package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        return filmService.update(newFilm);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long filmId, @PathVariable long userId) {
        filmService.removeLike(filmId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam("count") Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getPopularFilms(count.get());
        } else {
            return filmService.getPopularFilms(10);
        }
    }
}
