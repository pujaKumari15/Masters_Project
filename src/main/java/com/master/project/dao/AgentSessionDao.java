package com.master.project.dao;

import com.master.project.model.AgentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentSessionDao extends JpaRepository<AgentSession, String> {

    // Custom query to find AgentSessions by userId
    List<AgentSession> findByUserId(String userId);

    // Custom query to find the last created AgentSession
    AgentSession findTopByOrderByCreatedDateDesc();
}
