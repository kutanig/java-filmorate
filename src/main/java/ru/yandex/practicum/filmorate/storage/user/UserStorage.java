package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    Collection<User> getUsers();

    List<User> getFriends(Long userId);

    void addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

    User add(User user);

    User update(User newUser);

    boolean delete(Long userId);
}
