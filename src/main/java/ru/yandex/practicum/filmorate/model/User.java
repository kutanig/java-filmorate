package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    Long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Некорректная электронная почта")
    String email;
    @NotBlank(message = "Логин не может быть пустым")
    String login;
    String name;
    @Past(message = "Некорректная дата рождения")
    LocalDate birthday;
    @JsonIgnore
    Set<Long> friends = new HashSet<>();
}