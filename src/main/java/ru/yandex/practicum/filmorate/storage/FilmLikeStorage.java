package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;

public interface FilmLikeStorage {
    Map<Long, List<Long>> getFilmLike();
}
