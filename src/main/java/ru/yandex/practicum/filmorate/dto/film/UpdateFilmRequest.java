package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Director;
import validator.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UpdateFilmRequest {
    private long id;

    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
    private MpaRequest mpa;
    private List<GenreRequest> genres;
    private Set<Director> director;

    public boolean hasTitle() {
        return name != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasRatingId() {
        return mpa != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasDirector() {
        return director != null;
    }
}
