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
