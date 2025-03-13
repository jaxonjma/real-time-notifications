package com.gaming.events.service;

import com.gaming.events.dto.GameEventDTO;
import com.gaming.events.producer.GameEventProducer;
import org.springframework.stereotype.Service;

@Service
public class GameEventService {
    private final GameEventProducer producer;

    public GameEventService(GameEventProducer producer) {
        this.producer = producer;
    }

    public void sendLevelUpEvent(Long userId, int level) {
        GameEventDTO event = new GameEventDTO(userId, "LEVEL_UP", "Congratulations! You've reached level " + level + "!");
        producer.sendMessage(event);
    }

    public void sendItemAcquiredEvent(Long userId, String itemName) {
        GameEventDTO event = new GameEventDTO(userId, "ITEM_ACQUIRED", "You've acquired " + itemName + "!");
        producer.sendMessage(event);
    }

    public void sendFriendRequestEvent(Long senderId, Long receiverId) {
        GameEventDTO event = new GameEventDTO(receiverId, "FRIEND_REQUEST", "Player " + senderId + " has sent you a friend request.");
        producer.sendMessage(event);
    }
}
