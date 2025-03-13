package com.gaming.events.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.events.dto.GameEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GameEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "game-events";

    public GameEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(GameEventDTO event) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, jsonMessage);
            log.info("Sent message: {}", jsonMessage);
        } catch (Exception e) {
            log.error("Error sending Kafka message: {}", e.getMessage());
        }
    }
}
