package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<FilmLike> {

    public FilmLike mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmLike filmLike = new FilmLike();
        filmLike.setUserId(resultSet.getLong("user_id"));
        filmLike.setFilmId(resultSet.getLong("film_id"));
        filmLike.setCreateDate(resultSet.getTimestamp("CREATE_DATE").toInstant());
        return filmLike;
    }
}
