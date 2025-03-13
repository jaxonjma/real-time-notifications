package com.gaming.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.notification.model.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void registerSession(Long userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void sendNotification(Notification notification) {
        WebSocketSession session = sessions.get(notification.getUserId());
        if (session != null && session.isOpen()) {
            try {
                String message = objectMapper.writeValueAsString(notification);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}