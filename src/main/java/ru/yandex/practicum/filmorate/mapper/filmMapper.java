package ru.yandex.practicum.filmorate.mapper;

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
public class filmMapper {
    private MpaService mpaService;
    private GenreService genreService;

    public filmMapper(MpaService mpaService, GenreService genreService) {
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public Film toFilm(FilmDto dto) {
        Mpa mpa = mpaService.getMpa(dto.getMpaId());
        List<Genre> genres = Collections.emptyList();
        if (dto.getGenreIds() != null && !dto.getGenreIds().isEmpty()) {
            genres = dto.getGenreIds().stream()
                    .map(genreService::getGenre)
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
        Mpa mpa = film.getMpa();
        if (mpa == null) {
            throw new IllegalStateException("Фильм должен иметь MPA");
        }
        dto.setMpaId(mpa.getId());
        List<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            List<Long> genreIds = genres.stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList());
            dto.setGenreIds(genreIds);
        } else {
            dto.setGenreIds(Collections.emptyList());
        }
        return dto;
    }
}

