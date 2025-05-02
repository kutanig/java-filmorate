package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;

@Data
public class FilmLike {
    private Long filmId;
    private Long userId;
    private Instant createDate;
}
