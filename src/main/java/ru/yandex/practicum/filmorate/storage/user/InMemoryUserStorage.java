package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Friendship> findByFriendshipId(long userId, long friendId) {
        return friendshipRepository.findByFriendshipId(userId, friendId);
    }

    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.add(user);
    }

    public User update(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.update(user);
    }

    public boolean delete(Long userId) {
        return userRepository.delete(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(true);
        friendshipRepository.addFriend(friendship);

    }

    public Collection<User> getFriends(Long userId) {
        return friendshipRepository.getFriends(userId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        return friendshipRepository.deleteFriend(userId, friendId);
    }
}