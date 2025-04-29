package ru.yandex.practicum.filmorate.dal;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_GENRES_OF_FILM = """
            SELECT g.id, g.name
            FROM genres g
            JOIN film_genres fg ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            """;

    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    public Optional<Genre> findById(long genreId) {
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, genreId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    public List<Genre> findByFilmId(long filmId) {
        return jdbc.query(FIND_GENRES_OF_FILM, mapper, filmId);
    }
}
