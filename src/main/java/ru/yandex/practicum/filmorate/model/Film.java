package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import validator.ReleaseDateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film implements Comparable<Film> {
    Long id;

    @NotNull
    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @ReleaseDateValidation
    LocalDate releaseDate;

    @Positive
    Integer duration;

    Set<Long> likes = new HashSet<>();

    public void setLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film otherFilm) {
        return otherFilm.likes.size() - likes.size();
    }
}
