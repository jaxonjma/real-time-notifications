package com.gaming.notification.service;

import com.gaming.notification.model.UserPreferences;
import com.gaming.notification.repository.UserPreferencesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPreferencesServiceTest {

    @Mock
    private UserPreferencesRepository repository;

    @InjectMocks
    private UserPreferencesService userPreferencesService;

    private final Long userId = 1L;
    private UserPreferences userPreferences;

    @BeforeEach
    void setUp() {
        userPreferences = new UserPreferences(userId, true, true);
    }

    @Test
    @DisplayName("Should return existing preferences if found in repository")
    void shouldReturnExistingPreferences() {
        when(repository.findById(userId)).thenReturn(Optional.of(userPreferences));

        UserPreferences result = userPreferencesService.getPreferences(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertTrue(result.isGameEventsEnabled());
        assertTrue(result.isSocialEventsEnabled());

        verify(repository, times(1)).findById(userId);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should create and return default preferences if not found")
    void shouldCreateDefaultPreferencesIfNotFound() {
        when(repository.findById(userId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserPreferences result = userPreferencesService.getPreferences(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertTrue(result.isGameEventsEnabled());
        assertTrue(result.isSocialEventsEnabled());

        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).save(any(UserPreferences.class));
    }

    @Test
    @DisplayName("Should update user preferences correctly")
    void shouldUpdateUserPreferences() {
        when(repository.findById(userId)).thenReturn(Optional.of(userPreferences));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        userPreferencesService.updatePreferences(userId, false, true);

        assertFalse(userPreferences.isGameEventsEnabled());
        assertTrue(userPreferences.isSocialEventsEnabled());

        verify(repository, times(1)).save(userPreferences);
    }

    @Test
    @DisplayName("Should update preferences even if user had no previous ones")
    void shouldUpdatePreferencesIfNotFound() {
        when(repository.findById(userId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        userPreferencesService.updatePreferences(userId, false, false);

        verify(repository, times(2)).save(argThat(pref ->
                pref.getUserId().equals(userId) &&
                        !pref.isGameEventsEnabled() &&
                        !pref.isSocialEventsEnabled()
        ));
    }
}
