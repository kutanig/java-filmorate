package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> usersAll() {
        return users.values();
    }

    @PostMapping
    public void addUser(@RequestBody User newUser) {
        try {
            validatorUser(newUser);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        if (newUser.getName().isEmpty() || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Add user{}", newUser);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        try {
            if (user.getId() == null) {
                throw new ValidationException("Id должен быть указан");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        try {
            validatorUser(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        users.put(user.getId(), user);
        log.info("Update user{}", user);
    }

    private void validatorUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректная электронная почта");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
