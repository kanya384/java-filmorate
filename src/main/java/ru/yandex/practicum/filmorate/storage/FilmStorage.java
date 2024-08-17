package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    List<Film> findAll();

    Film getById(long id);

    Film update(Film film);

    List<Film> getPopularFilms(int count);
}
