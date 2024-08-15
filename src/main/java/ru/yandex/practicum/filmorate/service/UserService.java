package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> findAll();

    User create(User user);

    User getById(long id);

    User update(User user);

    User getUserById(long id);

    void addFriend(long firstUserId, long secondUserId);

    void removeFriend(long firstUserId, long secondUserId);

    Collection<User> getAllUsersFriends(long userId);

    Collection<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId);
}
