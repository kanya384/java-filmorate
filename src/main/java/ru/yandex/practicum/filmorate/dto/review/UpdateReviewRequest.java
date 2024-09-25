package ru.yandex.practicum.filmorate.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewRequest {
    private long id;
    private String content;
    private boolean isPositive;
}
