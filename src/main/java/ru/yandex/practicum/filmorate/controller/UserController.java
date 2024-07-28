package ru.yandex.practicum.filmorate.controller;

import exception.ConditionsNotMetException;
import exception.DuplicateDataException;
import exception.InvalidParameterException;
import exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            RuntimeException exception = new ConditionsNotMetException("имейл не может быть пустым");
            log.error("ошибка создания пользователя.", exception);
            throw exception;
        }

        if (isEmailExists(user.getEmail())) {
            RuntimeException exception = new DuplicateDataException("этот имейл уже используется");
            log.error("ошибка создания пользователя.", exception);
            throw exception;
        }

        if (isLoginExists(user.getLogin())) {
            RuntimeException exception = new DuplicateDataException("этот логин уже используется");
            log.error("ошибка создания пользователя.", exception);
            throw exception;
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            RuntimeException exception = new InvalidParameterException("дата рождения не может быть в будущем");
            log.error("ошибка создания пользователя.", exception);
            throw exception;
        }

        if (user.getLogin().contains(" ")) {
            RuntimeException exception = new InvalidParameterException("логин не может содержать пробелы");
            log.error("ошибка создания пользователя.", exception);
            throw exception;
        }

        user.setId(getNextId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            RuntimeException exception = new ConditionsNotMetException("id должен быть указан");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            RuntimeException exception = new NotFoundException(String.format("user с id = %d не найден",
                    newUser.getId()));
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        if (newUser.getEmail() != null && !oldUser.getEmail().equals(newUser.getEmail())
                && isEmailExists(newUser.getEmail())) {
            RuntimeException exception = new DuplicateDataException("этот имейл уже используется");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        if (newUser.getLogin() != null && !oldUser.getLogin().equals(newUser.getLogin())
                && isLoginExists(newUser.getEmail())) {
            RuntimeException exception = new DuplicateDataException("этот логин уже используется");
            log.error("ошибка обновления пользователя.", exception);
            throw exception;
        }

        if (newUser.getEmail() == null) {
            newUser.setEmail(oldUser.getEmail());
        }

        if (newUser.getLogin() == null) {
            newUser.setLogin(oldUser.getLogin());
        }

        if (newUser.getBirthday() == null) {
            newUser.setBirthday(oldUser.getBirthday());
        }

        users.put(newUser.getId(), newUser);

        return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean isEmailExists(String search) {
        return users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(search));
    }

    private boolean isLoginExists(String search) {
        return users.values()
                .stream()
                .map(User::getLogin)
                .anyMatch(login -> login.equals(search));
    }
}