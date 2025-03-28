package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> usersAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User deleteUserById(Long id) {
        return null;
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Add user {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException(String.format("Пользователя с id: %s не существует.", user.getId()));
        }
        users.put(user.getId(), user);
        log.debug("Update user {}", user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
