package ru.yandex.practicum.filmorate.service;

import exception.ConditionsNotMetException;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            RuntimeException exception = new ConditionsNotMetException("id должен быть указан");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        Film oldFilm = filmStorage.getById(film.getId());

        if (oldFilm == null) {
            RuntimeException exception = new NotFoundException("нет фильма с таким id");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }


        log.info("обновлен фильм {}", film);

        return filmStorage.update(film);
    }
}
