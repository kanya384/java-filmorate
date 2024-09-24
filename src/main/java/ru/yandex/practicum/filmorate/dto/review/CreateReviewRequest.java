package ru.yandex.practicum.filmorate.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewRequest {
    private String content;
    private boolean isPositive;
    private long userId;
    private long filmId;
}
