package com.master.project.controller;
import com.master.project.model.AgentMessage;
import com.master.project.model.AgentSession;
import com.master.project.service.AgentMessageService;
import com.master.project.service.AgentSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    // List AgentSessions by userId
    @GetMapping("/sessions/user/{userId}")
    public ResponseEntity<List<AgentSession>> getAgentSessionsByUserId(@PathVariable String userId) {
        List<AgentSession> sessions = agentSessionService.getAgentSessionsByUserId(userId);
        if (!sessions.isEmpty()) {
            return ResponseEntity.ok(sessions);
        }
        return ResponseEntity.notFound().build();
    }

    // Create an AgentMessage record
    @PostMapping("/messages")
    public ResponseEntity<AgentMessage> createAgentMessage(@RequestBody AgentMessage agentMessage) {
        AgentMessage createdMessage = agentMessageService.createAgentMessage(agentMessage);
        return ResponseEntity.ok(createdMessage);
    }

    // List AgentMessages by session_id, sorted by timestamp
    @GetMapping("/messages/session/{sessionId}")
    public ResponseEntity<List<AgentMessage>> getAgentMessagesBySessionId(@PathVariable String sessionId) {
        List<AgentMessage> messages = agentMessageService.getMessagesBySessionId(sessionId);
        if (!messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.notFound().build();
    }
}
