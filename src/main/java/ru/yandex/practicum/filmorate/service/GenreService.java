package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;

import java.util.List;

public interface GenreService {
    List<GenreResponse> findAll();

    GenreResponse getById(long id);
}
