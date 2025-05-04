package com.master.project.service;

import com.master.project.dao.AgentMessageDao;
import com.master.project.model.AgentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class AgentMessageService {

    @Autowired
    private AgentMessageDao agentMessageDao;

    // Create an AgentMessage record
    public AgentMessage createAgentMessage(AgentMessage agentMessage) {
        agentMessage.setId(UUID.randomUUID().toString());
        agentMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return agentMessageDao.save(agentMessage);
    }

    // Get messages by session_id, sorted by timestamp
    public List<AgentMessage> getMessagesBySessionId(String sessionId) {
        return agentMessageDao.findBySessionIdOrderByTimestamp(sessionId);
    }
}
