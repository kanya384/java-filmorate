package ru.yandex.practicum.filmorate.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private FilmService filmService;
    private UserService userService;
    private ReviewStorage reviewStorage;

    @Override
    public ReviewResponse create(CreateReviewRequest review) {
        try {
            filmService.getById(review.getFilmId());
        } catch (Exception e) {
            throw new NotFoundException("не найден фильм с id = " + review.getFilmId());
        }

        try {
            userService.getById(review.getUserId());
        } catch (Exception e) {
            throw new NotFoundException("не найден user с id = " + review.getFilmId());
        }

        Review result = reviewStorage.create(ReviewMapper.mapToReview(review));
        return ReviewMapper.mapToReviewResponse(result);
    }

    @Override
    public List<ReviewResponse> findAll(long filmId, int count) {
        return reviewStorage.findAll(filmId, count).stream().map(ReviewMapper::mapToReviewResponse).toList();
    }

    @Override
    public List<ReviewResponse> findAll(int count) {
        return reviewStorage.findAll(count).stream().map(ReviewMapper::mapToReviewResponse).toList();
    }

    @Override
    public ReviewResponse getById(long id) {
        return reviewStorage.getById(id).map(ReviewMapper::mapToReviewResponse)
                .orElseThrow(() -> new NotFoundException("не найден отзыв c id = " + id));
    }

    @Override
    public ReviewResponse update(UpdateReviewRequest request) {
        Review oldReview = reviewStorage.getById(request.getId())
                .orElseThrow(() -> new NotFoundException("не найден отзыв c id = " + request.getId()));

        Review updatedReview = ReviewMapper.updateReviewFields(oldReview, request);

        reviewStorage.update(updatedReview);
        return ReviewMapper.mapToReviewResponse(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewStorage.deleteReview(reviewId);
    }

    @Override
    public void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType) {
        reviewStorage.insertLikeDislikeToReview(reviewId, userId, likeType);
    }

    @Override
    public void removeLikeFromReview(Long filmId, Long userId, LikeType likeType) {
        reviewStorage.removeLikeFromReview(filmId, userId, likeType);
    }
}
