package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository

public class UserRepository extends BaseRepository<User> {


    private static final String FIND_ALL_QUERY = "SELECT * FROM \"USER\"";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM \"USER\" WHERE EMAIL = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"USER\" WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO \"USER\"(NAME, LOGIN, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE \"USER\" SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM \"USER\" WHERE ID = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    public List<User> findAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public boolean delete(long userId) {
        return delete(DELETE_BY_ID_QUERY, userId);
    }

    public User add(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday()
        );

        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }
}

