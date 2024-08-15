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

    Set<Long> likes;

    public void addLike(long userId) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film otherFilm) {
        int a = 0;
        int b = 0;
        if (likes != null) {
            a = likes.size();
        }

        if (otherFilm.likes != null) {
            b = otherFilm.likes.size();
        }

        return b - a;
    }
}
