package com.gaming.notification.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.notification.model.Notification;
import com.gaming.notification.service.NotificationService;
import com.gaming.notification.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(NotificationService notificationService, WebSocketService webSocketService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.webSocketService = webSocketService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "game-events", groupId = "notifications")
    public void listen(String message) {
        try {
            Notification notification = objectMapper.readValue(message, Notification.class);
            notificationService.saveNotification(notification);
            webSocketService.sendNotification(notification);
            log.info("Processed notification: {}", message);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
