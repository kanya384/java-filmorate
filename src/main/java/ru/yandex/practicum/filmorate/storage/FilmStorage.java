package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SearchFilter;

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

    List<Film> getPopularFilmsByGenreAndByDate(int count, int genreId, int year);

    void addDirectorToFilm(long filmId, long directorId);

    void deleteDirectorToFilm(long filmId);

    List<Film> getSortedFilmsOfDirector(long directorId, String sortBy);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> search(String query, SearchFilter by);
}
