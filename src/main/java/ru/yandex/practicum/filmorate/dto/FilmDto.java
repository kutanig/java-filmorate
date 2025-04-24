package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import java.time.LocalDate;
import java.util.List;

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
    @NotNull(message = "Длительность не может быть пустой")
    private Integer duration;
    @NotNull(message = "MPA не может быть пустым")
    private Long mpaId;
    private List<Long> genreIds;
}

