package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import validator.ReleaseDateValidation;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    Long id;

    @NotNull
    String name;

    @Size(max=200)
    String description;

    @ReleaseDateValidation
    LocalDate releaseDate;

    @Positive
    Integer duration;
}
