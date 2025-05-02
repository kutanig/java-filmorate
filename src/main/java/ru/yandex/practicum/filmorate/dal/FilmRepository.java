package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY =
            "SELECT f.*, m.name AS mpa_name, m.description AS mpa_description " +
                    "FROM films f " +
                    "JOIN mpa m ON f.mpa_id = m.id";

    private static final String FIND_BY_ID_QUERY =
            FIND_ALL_QUERY + " WHERE f.id = ?";

    private static final String GET_LIKES_COUNT =
            "SELECT COUNT(*) FROM film_likes WHERE film_id = ?";

    private static final String INSERT_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_FILM_GENRE =
            "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String UPDATE_QUERY =
            "UPDATE films SET " +
                    "name=?, description=?, release_date=?, duration=?, mpa_id=? " +
                    "WHERE id=?";

    private static final String DELETE_BY_ID_QUERY =
            "DELETE FROM films WHERE id = ?";

    private static final String DELETE_FILM_GENRES =
            "DELETE FROM film_genres WHERE film_id = ?";


    private final JdbcTemplate jdbc;
    private final RowMapper<Film> filmMapper;
    private final GenreRepository genreRepository;

    public FilmRepository(
            JdbcTemplate jdbc,
            RowMapper<Film> filmMapper,
            GenreRepository genreRepository
    ) {
        super(jdbc, filmMapper);
        this.jdbc = jdbc;
        this.filmMapper = filmMapper;
        this.genreRepository = genreRepository;
    }

    public List<Film> findAll() {
        List<Film> films = jdbc.query(FIND_ALL_QUERY, filmMapper);
        films.forEach(this::loadGenres);
        return films;
    }

    public Optional<Film> findById(Long filmId) {
        List<Film> films = jdbc.query(FIND_BY_ID_QUERY, filmMapper, filmId);
        if (films.isEmpty()) return Optional.empty();

        Film film = films.getFirst();
        loadGenres(film);
        return Optional.of(film);
    }

    //загрузка жанров
    private void loadGenres(Film film) {
        List<Genre> genres = genreRepository.findByFilmId(film.getId());
        film.setGenres(genres);
    }

    public Integer getLikesCount(Long filmId) {
        return jdbc.queryForObject(GET_LIKES_COUNT, Integer.class, filmId);
    }

    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        updateGenres(film);
        return film;
    }

    public Film update(Film film) {
        int rowsUpdated = jdbc.update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rowsUpdated == 0) {
            throw new ConflictException("Фильм с id=" + film.getId() + "не найден");
        }
        jdbc.update(DELETE_FILM_GENRES, film.getId());
        updateGenres(film);

        return findById(film.getId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден после обновления"));
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());

            jdbc.batchUpdate(INSERT_FILM_GENRE, uniqueGenres, uniqueGenres.size(),
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genre.getId());
                    });
        }
    }

    public boolean delete(Long filmId) {
        return jdbc.update(DELETE_BY_ID_QUERY, filmId) > 0;
    }
}

