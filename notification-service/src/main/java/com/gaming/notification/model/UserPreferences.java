package com.gaming.notification.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {
    @Id
    private Long userId;

    private boolean gameEventsEnabled;
    private boolean socialEventsEnabled;

}
