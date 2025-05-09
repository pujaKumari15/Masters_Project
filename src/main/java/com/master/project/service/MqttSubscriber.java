package com.master.project.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.project.dto.MqttMessageDto;
import com.master.project.enums.MqttCaller;
import com.master.project.model.Device;

@Service
public class MqttSubscriber {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MqttSubscriber.class);

    private final MqttClient mqttClient;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    public MqttSubscriber(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void subscribeToTopic(String topic) throws MqttException {
        log.info("MQTT Subscribing to topic: {}", topic);
        try {
            mqttClient.subscribe(topic, (t, msg) -> {
                String message = new String(msg.getPayload());
                log.info("MQTT Received on {}: {}", t, message);

                try {
                    MqttMessageDto mqttMessageDto = new ObjectMapper().readValue(message, MqttMessageDto.class);

                    if (mqttMessageDto.getCaller().equals(MqttCaller.IOT)) {

                        Device mqttDevice = new ObjectMapper().readValue(mqttMessageDto.getMessage(), Device.class);
                        // DeviceStatusDto status = new
                        // ObjectMapper().readValue(deviceMessage.getCurrentStatus(),
                        // DeviceStatusDto.class);

                        deviceService.updateDeviceStatus(mqttDevice.getId(), mqttDevice.getCurrentStatus());
                    }
                } catch (Exception e) {
                    log.error("Failed to parse MQTT message: {}", e.getMessage());
                }
            });
        } catch (MqttException e) {
            log.error("Failed to subscribe to topic {}: {}", topic, e.getMessage());
            throw e;
        }
    }
}
