package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmLikeStorage;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {
    FilmLikeStorage filmLikeStorage;

    @Override
    public Map<Long, List<Long>> getFilmLikes() {
        return filmLikeStorage.getFilmLike();
    }
}
