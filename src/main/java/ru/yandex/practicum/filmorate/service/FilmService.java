package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import java.util.List;

public interface FilmService {
    List<FilmResponse> findAll();

    FilmResponse create(NewFilmRequest film);

    FilmResponse getById(long id);

    FilmResponse update(long filmId, UpdateFilmRequest request);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    void removeFilm(Long filmId);

    List<FilmResponse> getPopularFilms(int count);


    List<FilmResponse> getPopularFilmsByGenreAndByDate(int count, int genreId, int year);

    List<FilmResponse> getSortedFilmsOfDirector(long directorId, String sortBy);

    List<FilmResponse> getCommonFilms(long userId, long friendId);

}
