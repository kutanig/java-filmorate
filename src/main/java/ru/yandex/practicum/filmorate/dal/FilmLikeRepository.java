package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {
    private static final String INSERT_QUERY = "INSERT INTO FILM_LIKE(FILM_ID, USER_ID, CREATE_DATE) VALUES (?, ?, ?)";
    private static final String DELETE_FILM_LIKE = "DELETE FROM FILM_LIKE WHERE FILM_ID = ? AND USER_ID = ?";

    public FilmLikeRepository(JdbcTemplate jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }

    public void add(Long filmId, Long userId) {
        Long generatedId = insert(
                INSERT_QUERY,
                filmId,
                userId,
                Timestamp.from(Instant.now())
        );
    }

    public boolean delete(Long filmId, Long userId) {
        int rowsDeleted = jdbc.update(DELETE_FILM_LIKE, filmId, userId);
        return rowsDeleted > 0;
    }
}