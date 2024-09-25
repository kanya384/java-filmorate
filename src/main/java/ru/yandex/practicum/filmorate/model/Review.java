package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Review {
    Long id;

    String content;

    @NotNull
    Boolean isPositive;

    @NotNull
    Long userId;

    @NotNull
    Long filmId;

    Integer useful;
}
