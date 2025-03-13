package com.gaming.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEventDTO {
    private Long userId;
    private String eventType;
    private String message;
}
