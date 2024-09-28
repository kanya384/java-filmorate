package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    Optional<User> getById(long id);

    User update(User user);

    void addFriendToUser(long userId, long friendId);

    void removeFriendOfUser(long userId, long friendId);

    void removeUserById(Long userId);

    void updateFriendshipStatus(long userId, long friendId, FriendshipStatus newStatus);

    List<User> getAllUsersFriends(long userId);

    List<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId);
}
