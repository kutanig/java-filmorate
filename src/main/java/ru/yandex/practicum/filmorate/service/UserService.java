package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    // Унифицированная проверка email
    private void validateEmailUniqueness(String email) {
        userStorage.getUserByEmail(email).ifPresent(u -> {
            throw new ConflictException("Email " + email + " уже занят");
        });
    }

    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return UserMapper.toDto(user);
    }

    public UserDto addUser(UserDto userDto) {
        validateEmailUniqueness(userDto.getEmail());
        return UserMapper.toDto(userStorage.add(UserMapper.toUser(userDto)));
    }

    public UserDto updateUser(UserDto newUser) {
        User oldUser = userStorage.getUserById(newUser.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден"));

        if (!oldUser.getEmail().equals(newUser.getEmail())) {
            validateEmailUniqueness(newUser.getEmail());
        }

        return UserMapper.toDto(userStorage.update(UserMapper.toUser(newUser)));
    }

    public Boolean deleteUser(Long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return userStorage.delete(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));

        if (userStorage.findByFriendshipId(userId, friendId).isPresent() ||
                userStorage.findByFriendshipId(friendId, userId).isPresent()) {
            throw new ConflictException("Пользователи уже являются друзьями");
        }

        userStorage.addFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return userStorage.getFriends(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<User> friends = new HashSet<>(getFriends(userId));
        return getFriends(otherUserId).stream()
                .filter(friends::contains)
                .collect(Collectors.toList());
    }
}
