package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final InMemoryFilmStorage filmStorage;

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