package com.gaming.notification.service;

import com.gaming.notification.model.Notification;
import com.gaming.notification.model.UserPreferences;
import com.gaming.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserPreferencesService preferencesService;

    @InjectMocks
    private NotificationService notificationService;

    private Notification gameNotification;
    private Notification socialNotification;
    private UserPreferences userPreferences;

    @BeforeEach
    void setUp() {
        gameNotification = new Notification(1L, "User 1 leveled up!", "GAME_EVENT", LocalDateTime.now());
        socialNotification = new Notification(1L, "User 1 received a friend request.", "SOCIAL_EVENT", LocalDateTime.now());

        userPreferences = new UserPreferences(1L, true, false);
    }

    @Test
    @DisplayName("Should return notifications for a given user")
    void shouldReturnNotificationsForUser() {
        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList(gameNotification, socialNotification));

        List<Notification> notifications = notificationService.getNotifications(1L);

        assertNotNull(notifications);
        assertEquals(2, notifications.size());
        assertEquals("GAME_EVENT", notifications.get(0).getType());
        assertEquals("SOCIAL_EVENT", notifications.get(1).getType());

        verify(notificationRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Should save game event notification if enabled")
    void shouldSaveGameEventNotificationIfEnabled() {
        when(preferencesService.getPreferences(1L)).thenReturn(userPreferences);

        notificationService.saveNotification(gameNotification);

        verify(notificationRepository, times(1)).save(gameNotification);
    }

    @Test
    @DisplayName("Should not save social event notification if disabled")
    void shouldNotSaveSocialEventNotificationIfDisabled() {
        when(preferencesService.getPreferences(1L)).thenReturn(userPreferences);

        notificationService.saveNotification(socialNotification);

        verify(notificationRepository, never()).save(socialNotification);
    }

    @Test
    @DisplayName("Should not save notification if user preferences are not found")
    void shouldNotSaveNotificationIfPreferencesNotFound() {
        when(preferencesService.getPreferences(1L)).thenReturn(null);

        notificationService.saveNotification(gameNotification);

        verify(notificationRepository, never()).save(any(Notification.class));
    }
}
