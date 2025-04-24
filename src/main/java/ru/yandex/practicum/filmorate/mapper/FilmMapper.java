package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmMapper {
    private final MpaService mpaService;
    private final GenreService genreService;

    public Film toFilm(FilmDto dto) {

        Mpa mpa = null;
        if (dto.getMpa() != null) {
            mpa = mpaService.getMpa(dto.getMpa().getId());
        }

        List<Genre> genres = Collections.emptyList();
        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            genres = dto.getGenres().stream()
                    .map(genre -> genreService.getGenre(genre.getId()))
                    .collect(Collectors.toList());
        }

        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    public FilmDto toDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        List<Genre> genres = film.getGenres();
        dto.setGenres(genres != null ? genres : Collections.emptyList());

        return dto;
    }
}

