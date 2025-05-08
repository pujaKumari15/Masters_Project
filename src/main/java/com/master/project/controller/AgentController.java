package com.master.project.controller;
import com.master.project.dto.AgentMessageDto;
import com.master.project.model.AgentMessage;
import com.master.project.model.AgentSession;
import com.master.project.service.AgentMessageService;
import com.master.project.service.AgentSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {

    @Autowired
    private AgentSessionService agentSessionService;

    @Autowired
    private AgentMessageService agentMessageService;

    // Create an AgentSession record
    @PostMapping("/sessions")
    public ResponseEntity<AgentSession> createAgentSession(@RequestBody AgentSession agentSession) {
        AgentSession createdSession = agentSessionService.createAgentSession(agentSession);
        return ResponseEntity.ok(createdSession);
    }

    // Get an AgentSession by ID
    @GetMapping("/sessions/{id}")
    public ResponseEntity<AgentSession> getAgentSessionById(@PathVariable String id) {
        AgentSession agentSession = agentSessionService.getAgentSessionById(id);
        if (agentSession != null) {
            return ResponseEntity.ok(agentSession);
        }
        return ResponseEntity.notFound().build();
    }

    // Get the last created AgentSession record
    @GetMapping("/sessions/last")
    public ResponseEntity<AgentSession> getLastAgentSession() {
        AgentSession lastSession = agentSessionService.getLastAgentSession();
        if (lastSession != null) {
            return ResponseEntity.ok(lastSession);
        }
        return ResponseEntity.notFound().build();
    }

    // Update an AgentSession name
    @PutMapping("/sessions/{id}")
    public ResponseEntity<AgentSession> updateAgentSession(@PathVariable String id, @RequestBody AgentSession agentSession) {
        AgentSession updatedSession = agentSessionService.updateAgentSessionName(id, agentSession.getName());
        if (updatedSession != null) {
            return ResponseEntity.ok(updatedSession);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete an AgentSession by ID
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteAgentSession(@PathVariable String id) {
        boolean deleted = agentSessionService.deleteAgentSession(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // List AgentSessions by userId
    @GetMapping("/sessions/user/{userId}")
    public ResponseEntity<List<AgentSession>> getAgentSessionsByUserId(@PathVariable String userId) {
        List<AgentSession> sessions = agentSessionService.getAgentSessionsByUserId(userId);
        return ResponseEntity.ok(sessions);
    }

    // List AgentMessages by session_id, sorted by timestamp
    @GetMapping("/messages/session/{sessionId}")
    public ResponseEntity<List<AgentMessageDto>> getAgentMessagesBySessionId(@PathVariable String sessionId) {
        List<AgentMessageDto> dtos = agentMessageService.getMessagesBySessionId(sessionId);
        return ResponseEntity.ok(dtos);
    }


    // Invoke the agent with a message
    @PostMapping("/invoke")
    public CompletableFuture<AgentMessageDto> invokeAgent(@RequestBody AgentMessage agentMessage) {
        if (agentMessage.getSessionId() == null || agentMessage.getSessionId().isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        agentSessionService.updateAgentSessionUpdateDate(agentMessage.getSessionId());
        agentMessageService.createAgentMessage(agentMessage);
        return agentMessageService.invokeAgent(agentMessage.getMessage(), agentMessage.getSessionId(), agentMessage.getUserId())
            .thenApply(AgentMessageDto::new);
    }

}
