package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    List<Film> findAll();

    Optional<Film> getById(long id);

    Film update(Film film);

    void addGenreToFilm(Long filmId, Long genreId);

    void clearGenresOfFilm(Long filmId);

    void addLikeToFilm(Long filmId, Long userId);

    void removeLikeFromFilm(Long filmId, Long userId);

    void removeFilmById(Long filmId);

    List<Film> getPopularFilms(int count);

    List<Film> getCommonFilms(long userId, long friendId);
}
