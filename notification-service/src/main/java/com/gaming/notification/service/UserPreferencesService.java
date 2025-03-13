package com.gaming.notification.service;

import com.gaming.notification.model.UserPreferences;
import com.gaming.notification.repository.UserPreferencesRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPreferencesService {
    private final UserPreferencesRepository repository;

    public UserPreferencesService(UserPreferencesRepository repository) {
        this.repository = repository;
    }

    public UserPreferences getPreferences(Long userId) {
        return repository.findById(userId).orElseGet(() -> {
            UserPreferences defaultPreferences = new UserPreferences(userId, true, true);
            repository.save(defaultPreferences);
            return defaultPreferences;
        });
    }

    public void updatePreferences(Long userId, boolean gameEventsEnabled, boolean socialEventsEnabled) {
        UserPreferences preferences = getPreferences(userId);
        preferences.setGameEventsEnabled(gameEventsEnabled);
        preferences.setSocialEventsEnabled(socialEventsEnabled);
        repository.save(preferences);
    }
}
