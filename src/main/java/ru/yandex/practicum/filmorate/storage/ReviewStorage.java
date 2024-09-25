package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review create(Review review);

    List<Review> findAll(long filmId, int count);

    List<Review> findAll(int count);

    Optional<Review> getById(long id);

    Review update(Review review);

    void deleteReview(Long reviewId);

    void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType);

    void removeLikeFromReview(Long filmId, Long userId, LikeType likeType);
}
