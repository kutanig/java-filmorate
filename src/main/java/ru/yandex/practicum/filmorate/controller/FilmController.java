package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> filmsAll() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Validated Film newFilm) {
        if (newFilm.getDuration().isNegative() || newFilm.getDuration().isZero()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Add film{}", newFilm);
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Validated Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с id: " + film.getId() + " не существует.");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        films.put(film.getId(), film);
        log.info("Update film{}", film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
