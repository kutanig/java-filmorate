package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private Long userId;
    private Long friendId;
    private boolean status;

    public Object getStatus() {
        return status;
    }
}
