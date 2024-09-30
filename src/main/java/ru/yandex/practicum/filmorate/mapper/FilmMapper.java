package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(NewFilmRequest request) {
        Film film = Film.builder()
                .title(request.getName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .releaseDate(request.getReleaseDate())
                .build();

        if (request.getMpa() != null) {
            film.setMpa(MpaRatingMapper.mapToMpaRating(request.getMpa()));
        }

        if (request.getGenres() != null) {
            film.setGenres(request.getGenres().stream().map(GenreMapper::mapToGenre).toList());
        }

        if (request.getDirector() != null) {
            film.setDirector(request.getDirector());
        }
        return film;
    }

    public static FilmResponse mapToFilmResponse(Film film) {
        FilmResponse filmResponse = FilmResponse.builder()
                .id(film.getId())
                .name(film.getTitle())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .build();
        if (film.getMpa() != null) {
            filmResponse.setMpa(MpaRatingMapper.mapToMpaRatingResponse(film.getMpa()));
        }
        if (film.getGenres() != null) {
            filmResponse.setGenres(film.getGenres().stream().map(GenreMapper::mapToGenreResponse).toList());
        }
        if (film.getDirector() != null) {
            filmResponse.setDirector(film.getDirector());
        }
        return filmResponse;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasTitle()) {
            film.setTitle(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasRatingId()) {
            film.setMpa(MpaRatingMapper.mapToMpaRating(request.getMpa()));
        }

        if (request.hasGenres()) {
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }

            film.setGenres(request.getGenres().stream().map(GenreMapper::mapToGenre).toList());
        } else {
            film.setGenres(null);
        }

        if (request.hasDirector()) {
            if (film.getDirector() == null) {
                film.setDirector(new HashSet<>());
            }
            film.setDirector(request.getDirector());
        } else {
            film.setDirector(null);
        }

        return film;
    }
}
