package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getAllFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto getFilm(
            @PathVariable @Positive(message = "ID фильма должен быть положительным числом") Long filmId
    ) {
        return filmService.getFilm(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Valid @RequestBody FilmDto filmDto) {
        return filmService.addFilm(filmDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto updateFilm(@Valid @RequestBody FilmDto filmDto) {
        return filmService.updateFilm(filmDto);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(
            @PathVariable @Positive(message = "ID фильма должен быть положительным числом") Long filmId
    ) {
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(
            @PathVariable @Positive(message = "ID фильма должен быть положительным числом") Long filmId,
            @PathVariable @Positive(message = "ID пользователя должен быть положительным числом") Long userId
    ) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(
            @PathVariable @Positive(message = "ID фильма должен быть положительным числом") Long filmId,
            @PathVariable @Positive(message = "ID пользователя должен быть положительным числом") Long userId
    ) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmDto> getPopularFilms(
            @RequestParam(defaultValue = "10") @Min(1) Integer count,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        return filmService.findMostPopular(count, from, sort);
    }
}
