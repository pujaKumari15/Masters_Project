package com.master.project.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.master.project.service.MqttSubscriber;

@Configuration
public class MqttConfig implements ApplicationContextAware {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MqttConfig.class);

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.client.id}")
    private String clientId;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);

        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectionLost(Throwable cause) {
                log.error("MQTT Connection lost: {}", cause.getMessage());
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (reconnect) {
                    log.info("MQTT Reconnected to broker: {}", serverURI);
                } else {
                    log.info("MQTT Connected to broker: {}", serverURI);
                }
                try {
                    MqttSubscriber subscriber = context.getBean(MqttSubscriber.class);
                    subscriber.subscribeToTopic("user/#");
                } catch (MqttException e) {
                    log.error("Failed to subscribe to topic: {}", e.getMessage());
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // This can be left empty; handled by MqttSubscriber
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });


        client.connect(options);
        return client;
    }
}
