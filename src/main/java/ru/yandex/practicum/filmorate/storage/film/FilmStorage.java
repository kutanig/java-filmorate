package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    Film add(Film film);

    Film update(Film newFilm);

    boolean delete(Long filmId);
}
