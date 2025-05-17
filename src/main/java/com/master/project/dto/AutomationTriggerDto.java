package com.master.project.dto;

import java.sql.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AutomationTriggerDto {
    private Date time;
    private String deviceId;
    private String attribute;
    private String operator;
    private Object value;

    public static AutomationTriggerDto fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, AutomationTriggerDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON for AutomationTriggerDto", e);
        }
    }

    // Getters and Setters
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}