package com.master.project.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dto.MqttMessageDto;
import com.master.project.enums.MqttAction;
import com.master.project.enums.MqttCaller;
import com.master.project.model.Device;

@Service
public class MqttPublisher {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MqttPublisher.class);

    @Autowired
    private MqttClient mqttClient;

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1);
        mqttClient.publish(topic, mqttMessage);
        log.info("MQTT Published to {}: {}", topic, message);
    }

    public void publish(MqttAction action, Device device, MqttCaller caller) throws MqttException {
        try {
            String deviceString = new ObjectMapper().writeValueAsString(device);
            MqttMessageDto mqttMessageDto = new MqttMessageDto(caller, deviceString);
            String message = new ObjectMapper().writeValueAsString(mqttMessageDto);
            String topic = "user/" + device.getOwnerId() + "/device/" + action.name().toLowerCase() + "/" + device.getId();
            publish(topic, message);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            log.error("MQTT Failed to convert Device object to JSON string", e);
            throw new MqttException(e);
        }
    }

}
