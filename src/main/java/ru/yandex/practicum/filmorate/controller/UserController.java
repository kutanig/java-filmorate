package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @GetMapping
    public Collection<User> usersAll() {
        log.info("GET/users");
        return userService.usersAll();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User newUser) {
        log.info("POST/users {}", newUser);
        return userService.addUser(newUser);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("PUT/users {}", user);
        return userService.updateUser(user);
    }
}
