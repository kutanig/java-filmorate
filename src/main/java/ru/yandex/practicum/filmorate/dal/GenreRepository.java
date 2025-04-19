package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRE WHERE ID = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRE";

    private static final String FIND_GENRES_OF_FILM = "SELECT g.ID, g.NAME FROM\n" +
            "GENRE g JOIN FILM_GENRE fg ON g.ID = fg.GENRE_ID \n" +
            "WHERE FG.FILM_ID = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper );
    }

    public Optional<Genre> findById(long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public List<Genre> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    public  List<Genre> findGenresOfFilm(long filmId) {
        return jdbc.query(FIND_GENRES_OF_FILM, mapper, filmId);
    }
}
