package com.master.project.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="agent_message")
public class AgentMessage {

    @Id
    private String id;
    private String userId;
    private String sessionId;
    private String message;
    private String actions;
    private String suggestions;
    private String query;
    private Timestamp timestamp;

    // Getters and setters


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
    
    public String getActions() {
        return actions;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMessage() {
        return message;
    }

    public String getQuery() {
        return query;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

