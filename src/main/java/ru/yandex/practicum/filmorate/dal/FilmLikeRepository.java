package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;

@Repository
public class FilmLikeRepository extends BaseRepository<FilmLike> {
    private static final String INSERT_QUERY = "INSERT INTO film_likes(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_FILM_LIKE = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    public FilmLikeRepository(JdbcTemplate jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }

    public void add(Long filmId, Long userId) {
        jdbc.update(INSERT_QUERY, filmId, userId);
    }

    public boolean delete(Long filmId, Long userId) {
        int rowsDeleted = jdbc.update(DELETE_FILM_LIKE, filmId, userId);
        return rowsDeleted > 0;
    }
}