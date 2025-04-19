package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @NotNull(message = "Надо указать логин")
    private String login;
    @Email(message = "Email не корректный")
    @NotBlank(message = "Email не может быть пустым")
    @NotNull(message = "Email не может быть пустым")
    private String email;
    private String name;
    @Past(message = "Не корректная дата рождения")
    private LocalDate birthday;
}

