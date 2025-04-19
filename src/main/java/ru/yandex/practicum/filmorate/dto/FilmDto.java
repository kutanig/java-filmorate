package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class FilmDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @MinimumDate
    private LocalDate releaseDate;
    @Positive(message = "Длительность должна быть положительной")
    private int duration;
    @NotNull(message = "MPA не может быть пустым")
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
}

