package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @ReleaseDateValidation
    LocalDate releaseDate;

    @Positive
    Integer duration;
}
