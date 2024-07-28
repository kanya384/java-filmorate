package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

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

    String description;
    LocalDate releaseDate;

    @Positive
    Integer duration;
}
