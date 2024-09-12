package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.mpa.MpaRatingResponse;

import java.util.List;

public interface MpaRatingService {
    List<MpaRatingResponse> findAll();

    MpaRatingResponse getById(long id);
}
