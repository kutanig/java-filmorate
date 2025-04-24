package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        mpa.setDescription(rs.getString("MPA_DESCRIPTION"));

        return Film.builder()
                .id(rs.getLong("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(mpa)
                // Жанры будут добавлены отдельным запросом
                .genres(new ArrayList<>())
                .build();
    }
}