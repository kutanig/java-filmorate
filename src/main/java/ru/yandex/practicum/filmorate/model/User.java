package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    @Email(message = "Некорректная электронная почта")
    String email;
    @NotNull
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    String login;
    String name;
    @Past(message = "Некорректная дата рождения")
    LocalDate birthday;
}
