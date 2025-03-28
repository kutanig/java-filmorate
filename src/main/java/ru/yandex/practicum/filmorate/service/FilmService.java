package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> filmsAll() {
        return filmStorage.filmsAll();
    }

    public Film addFilm(Film film) {
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        return filmStorage.updateFilm(film);
    }
}
