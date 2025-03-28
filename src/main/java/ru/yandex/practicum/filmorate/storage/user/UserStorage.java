package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> usersAll();

    User getUserById(Long id);

    User deleteUserById(Long id);

    User addUser(User user);

    User updateUser(User user);
}
