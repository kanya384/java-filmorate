package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User getById(long id);

    User update(User user);

    boolean isEmailExists(String search);

    boolean isLoginExists(String search);

    List<User> getAllUsersFriends(long userId);

    List<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId);
}
