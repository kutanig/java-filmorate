package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> filmsAll() {
        log.info("GET/films");
        return filmService.filmsAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film newFilm) {
        log.info("POST/films {}", newFilm);
        return filmService.addFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("PUT/films {}", film);
        return filmService.updateFilm(film);
    }
}
