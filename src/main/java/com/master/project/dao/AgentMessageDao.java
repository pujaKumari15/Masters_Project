package com.master.project.dao;

import com.master.project.model.AgentMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentMessageDao extends JpaRepository<AgentMessage, String> {

    // Custom query to find messages by sessionId, sorted by timestamp
    List<AgentMessage> findBySessionIdOrderByTimestamp(String sessionId);
}
