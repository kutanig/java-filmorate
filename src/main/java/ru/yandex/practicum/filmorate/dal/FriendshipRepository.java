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

    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM \"USER\" WHERE ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?)";
    private static final String FIND_FRIENDSHIP = "SELECT * FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";

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