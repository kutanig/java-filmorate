package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final FilmDbStorage filmStorage;

    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenre(Long genreId) {
        Optional<Genre> genre = filmStorage.getGenreById(genreId);
        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр с id = " + genreId + " не найден");
        }
        return genre.get();
    }
}