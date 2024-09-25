package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.Review;

@NoArgsConstructor
public class ReviewMapper {

    public static Review mapToReview(CreateReviewRequest request) {
        return Review.builder()
                .content(request.getContent())
                .isPositive(request.getIsPositive())
                .filmId(request.getFilmId())
                .userId(request.getUserId())
                .build();
    }

    public static ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .filmId(review.getFilmId())
                .userId(review.getUserId())
                .useful(review.getUseful())
                .build();
    }

    public static Review updateReviewFields(Review review, UpdateReviewRequest request) {
        review.setIsPositive(request.isPositive());

        if (request.getContent() != null) {
            review.setContent(request.getContent());
        }

        return review;
    }
}
