package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FilmLikeDbStorage implements FilmLikeStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String FILM_LIKES_QUERY = "SELECT user_id, film_id FROM film_likes";

    @Override
    public Map<Long, List<Long>> getFilmLike() {
        List<Map<String, Object>> filmLikes = jdbcTemplate.queryForList(FILM_LIKES_QUERY);
        Map<Long, List<Long>> likedFilmsByUserId = new HashMap<>();
        Map<Long, List<Long>> likedUsersByFilmId = new HashMap<>();


        for (Map<String, Object> filmLike : filmLikes) {
            long userId = (long) filmLike.get("user_id");
            long filmId = (long) filmLike.get("film_id");

            likedFilmsByUserId.computeIfAbsent(userId, k -> new ArrayList<>()).add(filmId);
            likedUsersByFilmId.computeIfAbsent(filmId, k -> new ArrayList<>()).add(userId);
        }

        return likedFilmsByUserId;
    }
}
