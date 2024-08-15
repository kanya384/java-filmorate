package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User getById(long id);

    User update(User user);

    boolean isEmailExists(String search);

    boolean isLoginExists(String search);

    Collection<User> getAllUsersFriends(long userId);

    Collection<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId);
}
