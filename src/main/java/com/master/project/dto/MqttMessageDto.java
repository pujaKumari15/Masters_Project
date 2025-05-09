package com.master.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.master.project.enums.MqttCaller;

public class MqttMessageDto {
    private MqttCaller caller;
    private String message;

    @JsonCreator
    public MqttMessageDto(
            @JsonProperty("caller") MqttCaller caller,
            @JsonProperty("message") String message) {
        this.caller = caller;
        this.message = message;
    }

    public MqttCaller getCaller() {
        return caller;
    }

    public void setCaller(MqttCaller caller) {
        this.caller = caller;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
