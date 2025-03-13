package com.gaming.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketServiceTest {

    private WebSocketService webSocketService;
    private WebSocketSession session;
    private Notification notification;

    @BeforeEach
    void setUp() {
        webSocketService = new WebSocketService();
        session = mock(WebSocketSession.class);
        notification = new Notification(1L, "New game event!", "GAME_EVENT");
    }

    @Test
    @DisplayName("Should register user session successfully")
    void shouldRegisterUserSession() {
        webSocketService.registerSession(1L, session);

        ConcurrentHashMap<Long, WebSocketSession> sessions = getSessionsFromService();
        assertNotNull(sessions);
        assertTrue(sessions.containsKey(1L));
        assertEquals(session, sessions.get(1L));
    }

    @Test
    @DisplayName("Should not send notification when session is closed")
    void shouldNotSendNotificationWhenSessionIsClosed() throws IOException {
        webSocketService.registerSession(1L, session);
        when(session.isOpen()).thenReturn(false);

        webSocketService.sendNotification(notification);

        verify(session, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    @DisplayName("Should not send notification when session is not registered")
    void shouldNotSendNotificationWhenSessionIsNotRegistered() throws IOException {
        webSocketService.sendNotification(notification);

        verify(session, never()).sendMessage(any(TextMessage.class));
    }

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<Long, WebSocketSession> getSessionsFromService() {
        try {
            var field = WebSocketService.class.getDeclaredField("sessions");
            field.setAccessible(true);
            return (ConcurrentHashMap<Long, WebSocketSession>) field.get(webSocketService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to access sessions field", e);
        }
    }
}
