package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;

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
