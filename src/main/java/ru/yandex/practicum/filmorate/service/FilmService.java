package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryUserStorage userStorage;
    private final InMemoryFilmStorage filmStorage;

    void validateFilmsData(FilmDto film) {
        if (filmStorage.getMPAById(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("MPA с id = " + film.getMpa().getId() + " не найден");
        }
        boolean isGenreNoExist = false;
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                Optional<Genre> genre = filmStorage.getGenreById(g.getId());
                if (genre.isEmpty()) {
                    isGenreNoExist = true;
                    break;
                }
            }
            if (isGenreNoExist) {
                throw new ValidationException("Жанр не найден");
            }
        }
    }

    public Collection<FilmDto> getFilms() {
        return filmStorage.getFilms().stream().map(FilmMapper::toDto).collect(Collectors.toList());
    }

    public FilmDto addFilm(FilmDto film) {
        //validateFilmsData(film);
        return FilmMapper.toDto(filmStorage.add(FilmMapper.toFilm(film)));
    }

    public FilmDto updateFilm(FilmDto newFilm) {
        Optional<Film> oldFilm = filmStorage.getFilmById(newFilm.getId());
        if (oldFilm.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }
        //validateFilmsData(newFilm);
        return FilmMapper.toDto(filmStorage.update(FilmMapper.toFilm(newFilm)));
    }

    public Boolean deleteFilm(Long filmId) {
        Optional<Film> oldFilm = filmStorage.getFilmById(filmId);
        if (oldFilm.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return filmStorage.delete(filmId);
    }

    public FilmDto getFilm(Long filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return FilmMapper.toDto(film.get());
    }

    public void addLike(Long filmId, Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isEmpty()) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<FilmDto> findMostPopular(Integer size, Integer from, String sort) {
        return filmStorage.getFilms().stream().sorted((p0, p1) -> {
            int comp = filmStorage.getLikesCount(p0).compareTo(filmStorage.getLikesCount(p1)); //прямой порядок сортировки
            if (sort.equals("desc")) {
                comp = -1 * comp;
            }
            return comp;
        }).skip(from).limit(size).map(FilmMapper::toDto).collect(Collectors.toList());
    }
}