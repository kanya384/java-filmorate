package ru.yandex.practicum.filmorate.service;

import exception.ConditionsNotMetException;
import exception.DuplicateDataException;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User getById(long id) {
        return userStorage.getById(id);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            RuntimeException exception = new ConditionsNotMetException("id должен быть указан");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        User oldUser = userStorage.getById(user.getId());
        if (oldUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    user.getId()));
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        if (!oldUser.getEmail().equals(user.getEmail())
                && userStorage.isEmailExists(user.getEmail())) {
            RuntimeException exception = new DuplicateDataException("этот имейл уже используется");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        if (!oldUser.getLogin().equals(user.getLogin())
                && userStorage.isLoginExists(user.getLogin())) {
            RuntimeException exception = new DuplicateDataException("этот логин уже используется");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }


        log.info("обновлен пользователь {}", user);

        return userStorage.update(user);
    }

    @Override
    public User getUserById(long id) {
        return userStorage.getById(id);
    }

    @Override
    public void addFriend(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getById(firstUserId);
        if (firstUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    firstUserId));
            log.error("ошибка добавления друга для пользователя.", exception);
            throw exception;
        }

        User secondUser = userStorage.getById(secondUserId);

        if (secondUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    secondUserId));
            log.error("ошибка добавления друга для пользователя.", exception);
            throw exception;
        }

        firstUser.addFriend(secondUserId);
        secondUser.addFriend(firstUserId);

        userStorage.update(firstUser);
        userStorage.update(secondUser);
    }

    @Override
    public void removeFriend(long firstUserId, long secondUserId) {
        User firstUser = userStorage.getById(firstUserId);
        if (firstUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    firstUserId));
            log.error("ошибка удаления друга пользователя.", exception);
            throw exception;
        }

        User secondUser = userStorage.getById(secondUserId);
        if (secondUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    secondUserId));
            log.error("ошибка удаления друга для пользователя.", exception);
            throw exception;
        }

        firstUser.removeFriend(secondUserId);
        secondUser.removeFriend(firstUserId);

        userStorage.update(firstUser);
        userStorage.update(secondUser);
    }

    @Override
    public Collection<User> getAllUsersFriends(long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    userId));
            log.error("ошибка получения друзей пользователя.", exception);
            throw exception;
        }
        return userStorage.getAllUsersFriends(userId);
    }

    @Override
    public Collection<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId) {
        return userStorage.getCommonFriendsOfUsers(firstUserId, secondUserId);
    }
}
