package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final InMemoryFilmStorage filmStorage;

    public Collection<Mpa> getMPAs() {
        return filmStorage.getMPAs();
    }

    public Mpa getMpa(Long mpaId) {
        Optional<Mpa> mpa = filmStorage.getMPAById(mpaId);
        if (mpa.isEmpty()) {
            throw new NotFoundException("MPA с id = " + mpaId + " не найден");
        }
        return mpa.get();
    }
}
