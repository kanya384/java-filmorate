package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import validator.NoWhiteSpacesValidation;

import java.time.LocalDate;

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
}
