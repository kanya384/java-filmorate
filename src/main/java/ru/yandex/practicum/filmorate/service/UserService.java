package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User create(User user);

    User getById(long id);

    User update(User user);

    User getUserById(long id);

    void addFriend(long firstUserId, long secondUserId);

    void removeFriend(long firstUserId, long secondUserId);

    List<User> getAllUsersFriends(long userId);

    List<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId);
}
