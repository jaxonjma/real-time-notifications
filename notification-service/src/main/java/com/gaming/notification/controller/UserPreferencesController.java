package com.gaming.notification.controller;

import com.gaming.notification.model.UserPreferences;
import com.gaming.notification.service.UserPreferencesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
public class UserPreferencesController {
    private UserPreferencesService preferencesService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserPreferences> getUserPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(preferencesService.getPreferences(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> updatePreferences(
            @PathVariable Long userId,
            @RequestParam boolean gameEventsEnabled,
            @RequestParam boolean socialEventsEnabled) {

        preferencesService.updatePreferences(userId, gameEventsEnabled, socialEventsEnabled);
        return ResponseEntity.ok("Preferences updated successfully");
    }
}
