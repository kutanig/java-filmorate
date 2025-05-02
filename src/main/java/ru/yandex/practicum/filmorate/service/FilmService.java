package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final MpaRepository mpaRepository;
    private final FilmMapper filmMapper;

    public void validateFilmData(FilmDto filmDto) {
        // Валидация MPA
        if (!mpaRepository.existsById(filmDto.getMpa().getId())) {
            throw new NotFoundException("MPA с id = " + filmDto.getMpa().getId() + " не найден");
        }
        // Валидация жанров
        boolean isGenreNoExist = false;
        if (filmDto.getGenres() != null) {
            for (Genre g : filmDto.getGenres()) {
                Optional<Genre> genre = filmStorage.getGenreById(g.getId());
                if (genre.isEmpty()) {
                    isGenreNoExist = true;
                    break;
                }
            }
            if (isGenreNoExist) {
                throw new NotFoundException("Жанр не найден");
            }
        }
    }

    public Collection<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(filmMapper::toDto)
                .collect(Collectors.toList());
    }

    public FilmDto addFilm(FilmDto filmDto) {
        validateFilmData(filmDto);
        return filmMapper.toDto(filmStorage.add(filmMapper.toFilm(filmDto)));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        filmStorage.getFilmById(filmDto.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        validateFilmData(filmDto);
        return filmMapper.toDto(filmStorage.update(filmMapper.toFilm(filmDto)));
    }

    public void deleteFilm(Long filmId) {
        if (!filmStorage.delete(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public FilmDto getFilm(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .map(filmMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        validateUserExists(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        validateUserExists(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    private void validateUserExists(Long userId) {
        if (!userStorage.getUserById(userId).isPresent()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    public Collection<FilmDto> findMostPopular(Integer size, Integer from, String sort) {
        return filmStorage.getFilms().stream().sorted((p0, p1) -> {
            int comp = filmStorage.getLikesCount(p0).compareTo(filmStorage.getLikesCount(p1)); //прямой порядок сортировки
            if (sort.equals("desc")) {
                comp = -1 * comp; //обратный порядок сортировки
            }
            return comp;
        }).skip(from).limit(size).map(filmMapper::toDto).collect(Collectors.toList());
    }
}