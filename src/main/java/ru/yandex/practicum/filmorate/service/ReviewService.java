package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.LikeType;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(CreateReviewRequest review);

    List<ReviewResponse> findAll(long filmId, int count);

    List<ReviewResponse> findAll(int count);

    ReviewResponse getById(long id);

    ReviewResponse update(UpdateReviewRequest review);

    void deleteReview(Long reviewId);

    void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType);

    void removeLikeFromReview(Long filmId, Long userId, LikeType likeType);
}
