package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<User, Set<User>> userFriends = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
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
            throw new NotFoundException(String.format("Пользователя с id: %s не существует.", user.getId()));
        }
        users.put(user.getId(), user);
        log.debug("Update user {}", user);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (users.get(id) == null || users.get(friendId) == null || id.equals(friendId)) {
            throw new ValidationException("Недопустимые пользователи для дружбы");
        }
        userFriends.computeIfAbsent(users.get(id), k -> new HashSet<>()).add(users.get(friendId));
        userFriends.computeIfAbsent(users.get(friendId), k -> new HashSet<>()).add(users.get(id));
        log.debug("Add friendship {} {}", users.get(id), users.get(friendId));
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        if (users.get(id) == null || users.get(friendId) == null) {
            throw new ValidationException("Пользователи не могут быть null");
        }
        if (userFriends.containsKey(users.get(id))) {
            userFriends.get(users.get(id)).remove(users.get(friendId));
        }
        if (userFriends.containsKey(users.get(friendId))) {
            userFriends.get(users.get(friendId)).remove(users.get(id));
        }
        log.debug("Remove friendship {} {}", users.get(id), users.get(friendId));
    }

    @Override
    public Collection<User> getFriends(Long id) {
        if (users.get(id) == null) {
            throw new ValidationException("Пользователь null");
        }
        return userFriends.getOrDefault(users.get(id), Collections.emptySet());
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long friendId) {
        if (users.get(id) == null || users.get(friendId) == null) {
            throw new ValidationException("Пользователи не могут быть null");
        }

        Set<User> friends1 = userFriends.getOrDefault(users.get(id), Collections.emptySet());
        Set<User> friends2 = userFriends.getOrDefault(users.get(friendId), Collections.emptySet());

        Set<User> mutualFriends = new HashSet<>(friends1);
        mutualFriends.retainAll(friends2);

        return mutualFriends;
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
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