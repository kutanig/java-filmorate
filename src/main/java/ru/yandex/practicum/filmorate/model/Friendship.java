package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private Long friendId; // ID друга
    private FriendshipStatus status; // Статус дружбы

    public enum FriendshipStatus {
        PENDING,
        CONFIRMED
    }
}
