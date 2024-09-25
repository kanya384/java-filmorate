package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(Review review);

    List<ReviewResponse> findAll(long filmId, int count);

    ReviewResponse getById(long id);

    ReviewResponse update(UpdateReviewRequest review);

    void upsertLikeToReview(Long reviewId, Long userId, LikeType likeType);

    void removeLikeFromReview(Long filmId, Long userId, LikeType likeType);
}
