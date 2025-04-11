package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден: " + user.getId());
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}", user);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.debug("Добавлена дружба между {} и {}", id, friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.debug("Удалена дружба между {} и {}", id, friendId);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long friendId) {
        Set<Long> commonIds = new HashSet<>(users.get(id).getFriends());
        commonIds.retainAll(users.get(friendId).getFriends());

        return commonIds.stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}