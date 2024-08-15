package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import validator.NoWhiteSpacesValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    Long id;

    @Email
    String email;

    @NoWhiteSpacesValidation
    String login;

    String name;

    @Past
    LocalDate birthday;

    Set<Long> friends;

    public Set<Long> getFriends() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }

    public void addFriend(long userId) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(userId);
    }

    public void removeFriend(long userId) {
        friends.remove(userId);
    }
}
