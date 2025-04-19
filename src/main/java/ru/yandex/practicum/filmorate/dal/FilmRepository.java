package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM FILM  ";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(*) FROM FILM_LIKE WHERE FILM_ID = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILM WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FILM(RATE, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRE = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE FILM SET RATE = ?, NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? WHERE ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM FILM WHERE ID = ?";
    private static final String DELETE_FILM_GENRES = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper, Film.class);
    }

    public List<Film> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }


    public Integer getLikesCount(Film film) {
        return jdbc.queryForObject(GET_LIKES_COUNT, Integer.class, film.getId());
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public boolean delete(long filmId) {
        return delete(DELETE_BY_ID_QUERY, filmId);
    }

    public void batchGenres(List<Genre> genres, Long filmId) {
        jdbc.batchUpdate(INSERT_FILM_GENRE,
                genres,
                100,
                (PreparedStatement ps, Genre genre) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genre.getId());
                });
    }

    public Film add(Film film) {

        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() != null) {
            List<Genre> result = film.getGenres().stream()
                    .distinct().toList();
            batchGenres(result, film.getId());
        }
        return film;
    }


    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        deleteFilmGenres(film.getId());
        if (film.getGenres() != null) {
            List<Genre> result = film.getGenres().stream()
                    .distinct().toList();
            batchGenres(result, film.getId());
        }
        return film;
    }

    public boolean deleteFilmGenres(Long filmId) {
        int rowsDeleted = jdbc.update(DELETE_FILM_GENRES, filmId);
        return rowsDeleted > 0;
    }
}

