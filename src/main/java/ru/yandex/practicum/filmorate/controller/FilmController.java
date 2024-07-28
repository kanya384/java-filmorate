package ru.yandex.practicum.filmorate.controller;

import exception.ConditionsNotMetException;
import exception.InvalidParameterException;
import exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            RuntimeException exception = new ConditionsNotMetException("название фильма не может быть пустым");
            log.error("ошибка создания фильма.", exception);
            throw exception;
        }

        if (film.getDescription().length() > 200) {
            RuntimeException exception = new InvalidParameterException("максимальная длина описания — 200 символов");
            log.error("ошибка создания фильма.", exception);
            throw exception;
        }

        if (film.getReleaseDate().isBefore(minDate)) {
            RuntimeException exception = new InvalidParameterException("дата релиза " +
                    "должна быть не раньше 28 декабря 1895 года");
            log.error("ошибка создания фильма.", exception);
            throw exception;
        }

        if (film.getDuration() < 0) {
            RuntimeException exception = new InvalidParameterException("продолжительность фильма " +
                    "должна быть положительным числом");
            log.error("ошибка создания фильма.", exception);
            throw exception;
        }

        film.setId(getNextId());

        films.put(film.getId(), film);

        log.info("создан фильм {}", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {

        if (newFilm.getId() == null) {
            RuntimeException exception = new ConditionsNotMetException("id должен быть указан");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        if (newFilm.getDescription().length() > 200) {
            RuntimeException exception = new InvalidParameterException("максимальная длина описания — 200 символов");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        if (newFilm.getReleaseDate().isBefore(minDate)) {
            RuntimeException exception = new InvalidParameterException("дата релиза " +
                    "должна быть не раньше 28 декабря 1895 года");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        if (newFilm.getDuration() < 0) {
            RuntimeException exception = new InvalidParameterException("продолжительность фильма " +
                    "должна быть положительным числом");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        Film oldFilm = films.get(newFilm.getId());

        if (oldFilm == null) {
            RuntimeException exception = new NotFoundException("нет фильма с таким id");
            log.error("ошибка обновления фильма.", exception);
            throw exception;
        }

        if (newFilm.getName() == null) {
            newFilm.setName(oldFilm.getName());
        }

        if (newFilm.getDescription() == null) {
            newFilm.setName(oldFilm.getDescription());
        }

        if (newFilm.getReleaseDate() == null) {
            newFilm.setReleaseDate(oldFilm.getReleaseDate());
        }

        if (newFilm.getDuration() == null) {
            newFilm.setDuration(oldFilm.getDuration());
        }

        films.put(newFilm.getId(), newFilm);

        log.info("обновлен фильм {}", newFilm);

        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
