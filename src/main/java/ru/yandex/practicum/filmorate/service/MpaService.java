package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final FilmDbStorage filmStorage;

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
