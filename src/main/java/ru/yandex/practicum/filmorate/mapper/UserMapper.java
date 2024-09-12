package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserResponse;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User mapToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        if (request.hasLogin()) {
            user.setLogin(request.getLogin());
        }

        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }


        return user;
    }
}
