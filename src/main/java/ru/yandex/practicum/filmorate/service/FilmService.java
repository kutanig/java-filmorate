package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryUserStorage userStorage;
    private final InMemoryFilmStorage filmStorage;
    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final ru.yandex.practicum.filmorate.mapper.filmMapper filmMapper;

    public void validateFilmData(FilmDto filmDto) {
        // Валидация MPA
        if (mpaRepository.findById(filmDto.getMpaId()).isEmpty()) {
            throw new ValidationException("MPA с id = " + filmDto.getMpaId() + " не найден");
        }

        // Валидация жанров
        if (filmDto.getGenreIds() != null && !filmDto.getGenreIds().isEmpty()) {
            List<Long> invalidGenres = filmDto.getGenreIds().stream()
                    .filter(id -> !genreRepository.existsById(id))
                    .collect(Collectors.toList());

            if (!invalidGenres.isEmpty()) {
                throw new ValidationException("Найдены несуществующие жанры: " + invalidGenres);
            }
        }
    }

    public Collection<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(this::enrichFilmWithDetails)
                .map(film -> filmMapper.toDto(film))
                .collect(Collectors.toList());
    }

    public FilmDto addFilm(FilmDto filmDto) {
        validateFilmData(filmDto);
        Film film = filmMapper.toFilm(filmDto);
        film.setMpa(mpaRepository.findById(filmDto.getMpaId())
                .orElseThrow(() -> new ValidationException("MPA не найден")));

        Film savedFilm = filmStorage.add(film);
        return filmMapper.toDto(enrichFilmWithDetails(savedFilm));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        Film existingFilm = filmStorage.getFilmById(filmDto.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        validateFilmData(filmDto);
        Film updatedFilm = filmMapper.toFilm(filmDto);
        updatedFilm.setMpa(existingFilm.getMpa());

        return filmMapper.toDto(filmStorage.update(updatedFilm));
    }

    public void deleteFilm(Long filmId) {
        if (!filmStorage.delete(filmId)) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public FilmDto getFilm(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .map(this::enrichFilmWithDetails)
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

    public List<FilmDto> getPopularFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(f -> -filmStorage.getLikesCount(f)))
                .limit(count)
                .map(this::enrichFilmWithDetails)
                .map(filmMapper::toDto)
                .collect(Collectors.toList());
    }

    private Film enrichFilmWithDetails(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            film.setGenres(genreRepository.findByFilmId(film.getId()));
        }
        if (film.getMpa() == null) {
            film.setMpa(mpaRepository.findById(film.getMpa().getId())
                    .orElseThrow(() -> new IllegalStateException("MPA не найден")));
        }
        return film;
    }

    private void validateUserExists(Long userId) {
        if (!userStorage.getUserById(userId).isPresent()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}