package ru.yandex.practicum.filmorate.dto.mpa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MpaRatingResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
}
