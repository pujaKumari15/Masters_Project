package com.master.project.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AutomationActionDto {
    private String deviceId;
    private String attribute;
    private Object value;

    public static AutomationActionDto fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, AutomationActionDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON for AutomationActionDto", e);
        }
    }

    // Getters and Setters
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}