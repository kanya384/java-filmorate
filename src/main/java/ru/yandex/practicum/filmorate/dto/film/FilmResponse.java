package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.dto.mpa.MpaRatingResponse;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class FilmResponse {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private MpaRatingResponse mpa;
    private List<GenreResponse> genres;
}
