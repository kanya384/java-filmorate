package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Collection<Film> findAll();

    Film create(Film film);

    Film getById(long id);

    Film update(Film film);

}
