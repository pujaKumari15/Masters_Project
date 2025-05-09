package com.master.project.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttSubscriber {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MqttSubscriber.class);

    private final MqttClient mqttClient;

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
            });
        } catch (MqttException e) {
            log.error("Failed to subscribe to topic {}: {}", topic, e.getMessage());
            throw e;
        }
    }
}
