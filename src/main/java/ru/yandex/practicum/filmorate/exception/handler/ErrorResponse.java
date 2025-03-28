package ru.yandex.practicum.filmorate.exception.handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}
