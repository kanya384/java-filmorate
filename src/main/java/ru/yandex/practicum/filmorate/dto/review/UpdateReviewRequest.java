package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewRequest {
    @JsonProperty("reviewId")
    private long id;
    private String content;
    private boolean isPositive;
}
