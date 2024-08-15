package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean isEmailExists(String search) {
        return users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(search));
    }

    @Override
    public boolean isLoginExists(String search) {
        return users.values()
                .stream()
                .map(User::getLogin)
                .anyMatch(login -> login.equals(search));
    }

    @Override
    public Collection<User> getAllUsersFriends(long userId) {
        return users.values().stream()
                .filter(user -> user.getFriends().contains(userId))
                .toList();
    }

    @Override
    public Collection<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId) {
        return users.values().stream()
                .filter(user -> user.getFriends().contains(firstUserId) && user.getFriends().contains(secondUserId))
                .toList();
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
