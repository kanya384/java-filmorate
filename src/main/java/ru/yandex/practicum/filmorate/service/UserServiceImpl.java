package ru.yandex.practicum.filmorate.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;
    private EventProcessor eventProcessor;

    @Override
    public List<UserResponse> findAll() {
        return userStorage.findAll().stream().map(UserMapper::mapToUserResponse).toList();
    }

    @Override
    public UserResponse create(NewUserRequest request) {
        return UserMapper.mapToUserResponse(userStorage.create(UserMapper.mapToUser(request)));
    }

    @Override
    public UserResponse getById(long id) {
        return userStorage.getById(id).map(UserMapper::mapToUserResponse)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        User updatedUser = userStorage.getById(request.getId())
                .map(film -> UserMapper.updateUserFields(film, request))
                .orElseThrow(() -> new NotFoundException("не найден пользователь с id = " + request.getId()));


        userStorage.update(updatedUser);

        log.info("обновлен пользователь {}", updatedUser);

        return UserMapper.mapToUserResponse(updatedUser);
    }

    @Override
    public UserResponse getUserById(long id) {
        return userStorage.getById(id)
                .map(UserMapper::mapToUserResponse)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с ID: " + id));
    }

    @Override
    public void addFriend(long firstUserId, long secondUserId) {
        Optional<User> user1 = userStorage.getById(firstUserId);
        if (user1.isEmpty()) {
            throw new NotFoundException("не найден пользователь с id = " + firstUserId);
        }

        Optional<User> user2 = userStorage.getById(secondUserId);
        if (user2.isEmpty()) {
            throw new NotFoundException("не найден пользователь с id = " + secondUserId);
        }
        userStorage.addFriendToUser(firstUserId, secondUserId);

        eventProcessor.add(secondUserId, firstUserId, EventType.FRIEND);
    }

    @Override
    public void removeFriend(long firstUserId, long secondUserId) {
        Optional<User> user1 = userStorage.getById(firstUserId);
        if (user1.isEmpty()) {
            throw new NotFoundException("не найден пользователь с id = " + firstUserId);
        }

        Optional<User> user2 = userStorage.getById(secondUserId);
        if (user2.isEmpty()) {
            throw new NotFoundException("не найден пользователь с id = " + secondUserId);
        }
        userStorage.removeFriendOfUser(firstUserId, secondUserId);

        eventProcessor.remove(secondUserId, firstUserId, EventType.FRIEND);
    }

    @Override
    public void removeUser(Long userId) {
        userStorage.removeUserById(userId);

        log.info("Пользователь с id = {} удален", userId);
    }

    @Override
    public List<UserResponse> getAllUsersFriends(long userId) {
        Optional<User> user1 = userStorage.getById(userId);
        if (user1.isEmpty()) {
            throw new NotFoundException("не найден пользователь с id = " + userId);
        }

        return userStorage.getAllUsersFriends(userId).stream()
                .map(UserMapper::mapToUserResponse).toList();
    }

    @Override
    public List<UserResponse> getCommonFriendsOfUsers(long firstUserId, long secondUserId) {
        return userStorage.getCommonFriendsOfUsers(firstUserId, secondUserId).stream()
                .map(UserMapper::mapToUserResponse).toList();
    }
}
