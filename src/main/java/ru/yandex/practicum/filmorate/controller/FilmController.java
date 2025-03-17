package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
    public void addFilm(@RequestBody Film newFilm) {
        try {
            validatorFilm(newFilm);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Add film{}", newFilm);
    }

    @PutMapping
    public void updateFilm(@RequestBody Film film) {
        try {
            if (film.getId() == null) {
                throw new ValidationException("Id должен быть указан");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        try {
            validatorFilm(film);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            return;
        }
        films.put(film.getId(), film);
        log.info("Update film{}", film);
    }

    private void validatorFilm(Film film) {
        if (film.getName() == null) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
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
