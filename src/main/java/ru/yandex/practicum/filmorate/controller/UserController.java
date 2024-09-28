package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.event.EventResponse;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userService;
    EventService eventService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserResponse findUserById(@PathVariable("id") long userId) {
        return userService.getUserById(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public UserResponse update(@Valid @RequestBody UpdateUserRequest newUser) {
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
    public List<UserResponse> getUserFriends(@PathVariable("id") long userId) {
        return userService.getAllUsersFriends(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserResponse> getCommonFriendsOfUsers(@PathVariable("id") long firstUserId, @PathVariable("otherId") long secondUserId) {
        return userService.getCommonFriendsOfUsers(firstUserId, secondUserId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable("id") long userId) {
        userService.removeUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/feed")
    public List<EventResponse> readEventFeedForUser(@PathVariable("id") long userId) {
        return eventService.readEventFeedForUser(userId);
    }

}