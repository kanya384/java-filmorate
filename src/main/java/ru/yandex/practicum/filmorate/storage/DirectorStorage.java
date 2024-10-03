package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director createDirector(Director director);

    List<Director> getAll();

    Director findById(long id);

    Director updateDirector(Director director);

    void deleteDirector(long id);

    List<Director> getDirectorsOfFilm(long filmId);
}
