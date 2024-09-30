package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse create(NewUserRequest user);

    UserResponse getById(long id);

    UserResponse update(UpdateUserRequest user);

    UserResponse getUserById(long id);

    void addFriend(long firstUserId, long secondUserId);

    void removeFriend(long firstUserId, long secondUserId);

    void removeUser(Long userId);

    List<UserResponse> getAllUsersFriends(long userId);

    List<UserResponse> getCommonFriendsOfUsers(long firstUserId, long secondUserId);

//    List<FilmResponse> getRecommendationsFilms(long userId);
    List<Long> getRecommendationsFilms(long userId);
}
