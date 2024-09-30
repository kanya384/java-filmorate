package ru.yandex.practicum.filmorate.service;

import java.util.List;
import java.util.Map;

public interface FilmLikeService {
    Map<Long, List<Long>> getFilmLikes();
}
