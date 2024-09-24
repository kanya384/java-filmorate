package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review create(Review review);

    List<Review> findAll();

    Optional<Review> getById(long id);

    Review update(Review film);

    void upsertLikeDislikeToReview(Long reviewId, Long userId, boolean isLike);

    void removeLikeDislikeFromReview(Long filmId, Long userId, boolean isLike);
}
