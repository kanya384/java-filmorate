package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;
import validator.NoWhiteSpacesValidation;

import java.time.LocalDate;

@Data
public class NewUserRequest {
    private String name;

    @NoWhiteSpacesValidation
    private String login;

    @Email
    private String email;

    @Past
    private LocalDate birthday;
}
