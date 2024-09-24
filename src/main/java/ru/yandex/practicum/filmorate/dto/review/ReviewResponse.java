package ru.yandex.practicum.filmorate.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReviewResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long reviewId;
    private String content;
    private boolean isPositive;
    private long userId;
    private long filmId;
    private int useful;
}
