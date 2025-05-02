package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Mpa> findById(long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    public List<Mpa> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM mpa WHERE id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
