package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> filmsAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        return null;
    }

    @Override
    public Film removeFilmById(Long id) {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Add film {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException(String.format("Фильма с id: %s не существует.", film.getId()));
        }
        films.put(film.getId(), film);
        log.debug("Update film {}", film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
