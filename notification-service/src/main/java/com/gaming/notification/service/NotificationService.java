package com.gaming.notification.service;

import com.gaming.notification.model.Notification;
import com.gaming.notification.model.UserPreferences;
import com.gaming.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final UserPreferencesService preferencesService;

    public NotificationService(NotificationRepository repository, UserPreferencesService preferencesService) {
        this.repository = repository;
        this.preferencesService = preferencesService;
    }

    public List<Notification> getNotifications(Long userId) {
        return repository.findByUserId(userId);
    }

    public void saveNotification(Notification notification) {
        UserPreferences preferences = preferencesService.getPreferences(notification.getUserId());

        boolean isGameEvent = "GAME_EVENT".equals(notification.getType());
        boolean isSocialEvent = "SOCIAL_EVENT".equals(notification.getType());

        if ((isGameEvent && preferences.isGameEventsEnabled()) ||
                (isSocialEvent && preferences.isSocialEventsEnabled())) {
            repository.save(notification);
        }
    }
}
