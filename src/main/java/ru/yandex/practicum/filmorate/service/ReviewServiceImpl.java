package ru.yandex.practicum.filmorate.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private FilmService filmService;
    private UserService userService;
    private ReviewStorage reviewStorage;
    private EventProcessor eventProcessor;

    @Override
    public ReviewResponse create(CreateReviewRequest review) {
        filmService.getById(review.getFilmId());
        userService.getById(review.getUserId());

        Review result = reviewStorage.create(ReviewMapper.mapToReview(review));
        eventProcessor.add(result.getId(), result.getUserId(), EventType.REVIEW);
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
        eventProcessor.update(updatedReview.getId(), updatedReview.getUserId(), EventType.REVIEW);
        return ReviewMapper.mapToReviewResponse(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Optional<Review> review = reviewStorage.getById(reviewId);
        if (review.isPresent()) {
            reviewStorage.deleteReview(reviewId);
            eventProcessor.remove(reviewId, review.get().getUserId(), EventType.REVIEW);
        }
    }

    @Override
    public void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType) {
        reviewStorage.insertLikeDislikeToReview(reviewId, userId, likeType);
    }

    @Override
    public void removeLikeFromReview(Long reviewId, Long userId, LikeType likeType) {
        reviewStorage.removeLikeFromReview(reviewId, userId, likeType);
    }
}
