package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Collection<Film> findAll();

    Film getById(long id);

    Film update(Film film);

    Collection<Film> getPopularFilms(int count);
}
