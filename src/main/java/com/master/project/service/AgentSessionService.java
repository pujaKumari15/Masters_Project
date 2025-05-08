package com.master.project.service;

import com.master.project.dao.AgentMessageDao;
import com.master.project.dao.AgentSessionDao;
import com.master.project.model.AgentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class AgentSessionService {
    @Autowired
    private AgentSessionDao agentSessionDao;

    @Autowired
    private AgentMessageDao agentMessageDao;

    // Create an AgentSession record
    public AgentSession createAgentSession(AgentSession agentSession) {
        agentSession.setId(UUID.randomUUID().toString());
        agentSession.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        agentSession.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return agentSessionDao.save(agentSession);
    }

    // Get AgentSession by ID
    public AgentSession getAgentSessionById(String id) {
        return agentSessionDao.findById(id).orElse(null);
    }

    // Get the last created AgentSession
    public AgentSession getLastAgentSession() {
        return agentSessionDao.findTopByOrderByCreatedDateDesc();
    }

    // Get AgentSessions by userId
    public List<AgentSession> getAgentSessionsByUserId(String userId) {
        return agentSessionDao.findByUserId(userId);
    }

    // Update an AgentSession name
    public AgentSession updateAgentSessionName(String id, String name) {
        AgentSession agentSession = agentSessionDao.findById(id).orElse(null);
        if (agentSession != null) {
            agentSession.setName(name);
            agentSession.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            return agentSessionDao.save(agentSession);
        }
        return null;
    }

    // Update an AgentSession update date
    public AgentSession updateAgentSessionUpdateDate(String id) {
        AgentSession agentSession = agentSessionDao.findById(id).orElse(null);
        if (agentSession != null) {
            agentSession.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
            return agentSessionDao.save(agentSession);
        }
        return null;
    }

    // Delete an AgentSession by ID
    @Transactional
    public boolean deleteAgentSession(String id) {
        // delete all messages associated with this session
        agentMessageDao.deleteBySessionId(id);
        if (agentSessionDao.existsById(id)) {
            agentSessionDao.deleteById(id);
            return true;
        }
        return false;
    }
}
