package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return userService.update(newUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long firstUserId, @PathVariable("friendId") long secondUserId) {
        userService.addFriend(firstUserId, secondUserId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        userService.removeFriend(userId, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") long userId) {
        return userService.getAllUsersFriends(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsOfUsers(@PathVariable("id") long firstUserId, @PathVariable("otherId") long secondUserId) {
        return userService.getCommonFriendsOfUsers(firstUserId, secondUserId);
    }

}