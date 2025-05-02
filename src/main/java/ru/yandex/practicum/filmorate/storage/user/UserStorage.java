package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getUsers();

    List<User> getFriends(Long userId);

    void addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    User add(User user);

    User update(User newUser);

    boolean delete(Long userId);

    Optional<User> getUserById(long userId);

    Optional<User> getUserByEmail(String email);

    Optional<Friendship> findByFriendshipId(long userId, long friendId);
}
