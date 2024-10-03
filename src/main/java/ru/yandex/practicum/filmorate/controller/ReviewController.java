package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.review.CreateReviewRequest;
import ru.yandex.practicum.filmorate.dto.review.ReviewResponse;
import ru.yandex.practicum.filmorate.dto.review.UpdateReviewRequest;
import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReviewResponse> getReviewsOfFilm(@RequestParam(required = false) Optional<Long> filmId, @RequestParam(defaultValue = "10") Integer count) {
        if (filmId.isPresent()) {
            return reviewService.findAll(filmId.get(), count);
        } else {
            return reviewService.findAll(count);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ReviewResponse getReviewsById(@PathVariable("id") Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReviewResponse create(@RequestBody @Valid CreateReviewRequest request) {
        return reviewService.create(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReviewResponse update(@RequestBody @Valid UpdateReviewRequest request) {
        return reviewService.update(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        reviewService.insertLikeDislikeToReview(reviewId, userId, LikeType.LIKE);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        reviewService.insertLikeDislikeToReview(reviewId, userId, LikeType.DISLIKE);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        reviewService.removeLikeFromReview(reviewId, userId, LikeType.LIKE);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable("id") long reviewId, @PathVariable long userId) {
        reviewService.removeLikeFromReview(reviewId, userId, LikeType.DISLIKE);
    }
}
