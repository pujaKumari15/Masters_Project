package com.master.project.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dao.AgentMessageDao;
import com.master.project.dto.AgentMessageDto;
import com.master.project.enums.MqttCaller;
import com.master.project.model.AgentMessage;
import com.master.project.model.Device;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AgentMessageService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AgentMessageService.class);

    @Value("${aws.bedrock.agentid}")
    private String agentId;
    @Value("${aws.bedrock.agentaliasid}")
    private String agentAliasId;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AgentMessageDao agentMessageDao;

    @Autowired
    private BedrockAgentRuntimeAsyncClient bedrockClient;

    public AgentMessage createAgentMessage(AgentMessage agentMessage) {
        agentMessage.setId(UUID.randomUUID().toString());
        agentMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return agentMessageDao.save(agentMessage);
    }
    
    public List<AgentMessageDto> getMessagesBySessionId(String sessionId) {
        List<AgentMessage> messages = agentMessageDao.findBySessionIdOrderByTimestamp(sessionId);
        return messages.stream()
                       .map(AgentMessageDto::new)
                       .collect(Collectors.toList());
    }

    public CompletableFuture<AgentMessage> invokeAgent(String inputText, String sessionId, String userId) {
        CompletableFuture<AgentMessage> futureResponse = new CompletableFuture<>();

        String prompt = "<user_id>" + userId + "</user_id> " + inputText;

        log.info("Invoking agent with prompt: {}", prompt);

        InvokeAgentRequest request = InvokeAgentRequest.builder()
            .agentId(agentId)
            .agentAliasId(agentAliasId)
            .sessionId(sessionId)
            .memoryId(sessionId)
            .overrideConfiguration(o -> o.apiCallAttemptTimeout(Duration.of(60, ChronoUnit.SECONDS)))
            .inputText(prompt)
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

                        // Escape inner quotes inside the query string if present
                        Pattern pattern = Pattern.compile("\"query\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
                        Matcher matcher = pattern.matcher(json);
                        StringBuffer sb = new StringBuffer();

                        while (matcher.find()) {
                            String originalQuery = matcher.group(1);
                            String escapedQuery = originalQuery.replace("\"", "\\\\\"");
                            matcher.appendReplacement(sb, "\"query\":\"" + escapedQuery + "\"");
                        }
                        matcher.appendTail(sb);
                        json = sb.toString();

                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

                        int startIdx = json.indexOf('{');
                        if (startIdx == -1) {
                            throw new JsonProcessingException("No JSON object found in response") {};
                        }
                        String jsonPart = json.substring(startIdx);
                        JsonNode root = mapper.readTree(jsonPart);

                        String message = root.path("message").asText();
                        JsonNode actionsNode = root.path("actions");
                        JsonNode suggestionsNode = root.path("suggestions");
                        String query = root.path("query").asText();

                        // parse the actions and loop checking for the action type
                        // [{"action":"device_update","id":"6a386c98-fec7-4d0d-9c27-f27303b24dc8","status":{"power":"off"}}]
                        if (actionsNode.isArray()) {
                            for (JsonNode action : actionsNode) {
                                String actionType = action.path("action").asText();
                                log.info("Action type: {}", actionType);

                                if (actionType.equals("device_update")) {
                                    String deviceId = action.path("id").asText();
                                    JsonNode statusNode = action.path("status");
                                    log.info("Device ID: {}", deviceId);
                                    Device existingDevice = deviceService.getDeviceById(deviceId).orElse(null);
                                    if (existingDevice != null) {
                                        String statusJson = statusNode.toString();
                                        log.info("Updating device status: {}", statusJson);
                                        existingDevice.setCurrentStatus(statusJson);
                                        deviceService.updateDevice(deviceId, existingDevice, MqttCaller.AGENT);
                                    } else {
                                        log.warn("Device with ID {} not found", deviceId);
                                    }
                                }

                            }
                        }

                        AgentMessage agentMessage = new AgentMessage();
                        agentMessage.setId(UUID.randomUUID().toString());
                        agentMessage.setSessionId(sessionId);
                        agentMessage.setUserId(null);
                        agentMessage.setMessage(message);
                        agentMessage.setActions(actionsNode.toString());
                        agentMessage.setSuggestions(suggestionsNode.toString());
                        agentMessage.setQuery(query);
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
