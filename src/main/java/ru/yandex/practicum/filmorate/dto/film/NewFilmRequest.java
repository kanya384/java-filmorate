package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import validator.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NewFilmRequest {
    @NotNull
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
    private MpaRequest mpa;
    private List<GenreRequest> genres;
}