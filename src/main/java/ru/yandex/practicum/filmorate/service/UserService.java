package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(Long id, Long friendId) {
        validateUserIds(id, friendId);
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        validateUserIds(id, friendId);
        userStorage.removeFriend(id, friendId);
    }

    public Collection<User> getFriends(Long id) {
        validateUserId(id);
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        validateUserId(id);
        validateUserId(friendId);
        return userStorage.getCommonFriends(id, friendId);
    }

    private void validateUserIds(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new ValidationException("Нельзя выполнить операцию с одинаковыми ID");
        }
        if (!userStorage.existsById(id) || !userStorage.existsById(friendId)) {
            throw new NotFoundException("Один из пользователей не найден");
        }
    }

    private void validateUserId(Long id) {
        if (!userStorage.existsById(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}