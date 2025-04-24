package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Slf4j
@Qualifier
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final FilmLikeRepository filmLikeRepository;

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Optional<Film> getFilmById(long filmId) {
        return filmRepository.findById(filmId);
    }

    public Film add(Film film) {
        return filmRepository.add(film);
    }

    public Film update(Film film) {
        return filmRepository.update(film);
    }

    public boolean delete(Long filmId) {
        return filmRepository.delete(filmId);
    }

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getGenreById(long genreId) {
        return genreRepository.findById(genreId);
    }

    public Optional<Mpa> getMPAById(long mpaId) {
        return mpaRepository.findById(mpaId);
    }

    public List<Mpa> getMPAs() {
        return mpaRepository.findAll();
    }

    public Integer getLikesCount(Film film) {
        return filmRepository.getLikesCount(film.getId());
    }

    public void addLike(Long filmId, Long userId) {

        filmLikeRepository.add(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmLikeRepository.delete(filmId, userId);
    }

}
