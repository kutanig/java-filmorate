package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @MinimumDate
    LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @NotNull(message = "Не может быть пустым")
    Duration duration;
}
