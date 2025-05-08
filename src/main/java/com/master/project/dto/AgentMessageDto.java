package com.master.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.model.AgentMessage;

import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL) // optional: to ignore null fields
public class AgentMessageDto {

    private String id;
    private String userId;
    private String sessionId;
    private String message;
    private Object actions;
	private Object suggestions;
    private Timestamp timestamp;

    public AgentMessageDto() {} // REQUIRED for Jackson

    public AgentMessageDto(AgentMessage agentMessage) {
        this.id          = agentMessage.getId();
        this.userId      = agentMessage.getUserId();
        this.sessionId   = agentMessage.getSessionId();
        this.message     = agentMessage.getMessage();
        this.actions     = parseJson(agentMessage.getActions());
		this.suggestions = parseJson(agentMessage.getSuggestions());
        this.timestamp   = agentMessage.getTimestamp();
    }

    private Object parseJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Object.class); // Use real class if available
        } catch (Exception e) {
            return null;
        }
    }

    // Getters for Jackson

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public String getMessage() { return message; }
	public Object getActions() { return actions; }
    public Object getSuggestions() { return suggestions; }
    public Timestamp getTimestamp() { return timestamp; }
}
