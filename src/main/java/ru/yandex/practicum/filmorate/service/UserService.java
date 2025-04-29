package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.RepositoryException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Friendship;
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

    void validateUsersData(UserDto user) {

        Optional<User> currentUser = userStorage.getUserByEmail(user.getEmail());
        if (currentUser.isPresent()) {
            throw new ValidationException("Этот имейл уже используется");
        }
    }

    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return UserMapper.toDto(user.get());
    }

    public UserDto addUser(UserDto user) {
        validateUsersData(user);
        return UserMapper.toDto(userStorage.add(UserMapper.toUser(user)));
    }

    public UserDto updateUser(UserDto newUser) {
        Optional<User> oldUser = userStorage.getUserById(newUser.getId());
        if (oldUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        if (!Objects.equals(oldUser.get().getEmail(), newUser.getEmail())) {
            Optional<User> currentUser = userStorage.getUserByEmail(newUser.getEmail());
            if (currentUser.isPresent()) {
                try {
                    throw new RepositoryException("Этот имейл уже используется");
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return UserMapper.toDto(userStorage.update(UserMapper.toUser(newUser)));
    }

    public Boolean deleteUser(Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userStorage.delete(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + " нет");
        }
        Optional<User> friend = userStorage.getUserById(friendId);
        if (friend.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + friendId + " нет");
        }
        Optional<Friendship> friendship = userStorage.findByFriendshipId(userId, friendId);
        if (friendship.isPresent()) {
            throw new NotFoundException("Пользователь с id = \" + userId + \" уже добавил пользователя \" + friendId + \"в друзья");
        }
        Optional<Friendship> friendship1 = userStorage.findByFriendshipId(friendId, userId);
        if (friendship1.isPresent()) {
            throw new NotFoundException("Пользователь с id = " + friendId + " уже добавил пользователя " + userId + "в друзья");
        }
        userStorage.addFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + " нет");
        }

        return userStorage.getFriends(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + userId + " нет");
        }
        Optional<User> friend = userStorage.getUserById(friendId);
        if (friend.isEmpty()) {
            throw new NotFoundException("пользователя с id = " + friendId + " нет");
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<User> secondFriends = new HashSet<>(getFriends(otherUserId));
        return getFriends(userId).stream()
                .filter(secondFriends::contains)
                .collect(Collectors.toList());
    }
}
