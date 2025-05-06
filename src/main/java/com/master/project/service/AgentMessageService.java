package com.master.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dao.AgentMessageDao;
import com.master.project.model.AgentMessage;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.InvokeAgentResponseHandler;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AgentMessageService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AgentMessageService.class);

    @Autowired
    private AgentMessageDao agentMessageDao;

    @Autowired
    private BedrockAgentRuntimeAsyncClient bedrockClient;

    public AgentMessage createAgentMessage(AgentMessage agentMessage) {
        agentMessage.setId(UUID.randomUUID().toString());
        agentMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return agentMessageDao.save(agentMessage);
    }

    public List<AgentMessage> getMessagesBySessionId(String sessionId) {
        return agentMessageDao.findBySessionIdOrderByTimestamp(sessionId);
    }

    public CompletableFuture<AgentMessage> invokeAgent(String inputText, String sessionId) {
        CompletableFuture<AgentMessage> futureResponse = new CompletableFuture<>();

        String agentId      = "";
        String agentAliasId = "";

        InvokeAgentRequest request = InvokeAgentRequest.builder()
            .agentId(agentId)
            .agentAliasId(agentAliasId)
            .sessionId(sessionId)
            .memoryId(sessionId)
            .overrideConfiguration(o -> o.apiCallAttemptTimeout(Duration.of(60, ChronoUnit.SECONDS)))
            .inputText(inputText)
            .build();

        StringBuilder responseString = new StringBuilder();

        bedrockClient.invokeAgent(
            request,
            InvokeAgentResponseHandler.builder()
                .subscriber(InvokeAgentResponseHandler.Visitor.builder()
                    .onChunk(chunk -> {
                        String chunkText = chunk.bytes().asUtf8String();
                        log.info("Received chunk: {}", chunkText);
                        responseString.append(chunkText);
                    })
                    .build())
                .onError(throwable -> {
                    log.error("Error during agent invocation: {}", throwable.getMessage());
                    futureResponse.completeExceptionally(throwable);
                })
                .onComplete(() -> {
                    try {
                        String json = responseString.toString()
                            .replace("\n", " ")
                            .replace("\r", " ");
                        log.info("Final raw response: {}", json);

                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(json);

                        String message = root.path("message").asText();
                        JsonNode actionsNode = root.path("actions");
                        JsonNode suggestionsNode = root.path("suggestions");

                        AgentMessage agentMessage = new AgentMessage();
                        agentMessage.setId(UUID.randomUUID().toString());
                        agentMessage.setSessionId(sessionId);
                        agentMessage.setUserId(null);
                        agentMessage.setMessage(message);
                        agentMessage.setActions(actionsNode.toString());
                        agentMessage.setSuggestions(suggestionsNode.toString());
                        agentMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
                        agentMessageDao.save(agentMessage);
                        futureResponse.complete(agentMessage);

                    } catch (JsonProcessingException e) {
                        log.error("Failed to parse response JSON", e);
                        futureResponse.completeExceptionally(e);
                    }
                })
                .build()
        );

        return futureResponse;
    }
}
