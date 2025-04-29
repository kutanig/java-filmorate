package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {

    private static final String FIND_FRIENDS_QUERY = """
            SELECT u.*
            FROM users u
            JOIN friendships f ON u.id = f.friend_id
            WHERE f.user_id = ?""";
    private static final String FIND_FRIENDSHIP = "SELECT * FROM friendships WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friendships(user_id, friend_id, status) VALUES (?, ?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public Friendship addFriend(Friendship friendship) {

        insertWithPrimaryKey(
                INSERT_QUERY,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus()
        );
        return friendship;
    }

    public Optional<Friendship> findByFriendshipId(long userId, long friendId) {
        return findOne(FIND_FRIENDSHIP, userId, friendId);
    }

    public List<User> getFriends(long userId) {
        RowMapper<User> userRowMapper = new UserRowMapper();
        return jdbc.query(FIND_FRIENDS_QUERY, userRowMapper, userId);

    }

    public boolean deleteFriend(long userId, long friendId) {
        int rowsDeleted = jdbc.update(DELETE_FRIENDSHIP_QUERY, userId, friendId);
        return rowsDeleted > 0;
    }
}