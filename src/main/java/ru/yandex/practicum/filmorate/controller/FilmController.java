package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
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
    public List<FilmResponse> findAll() {
        return filmService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public FilmResponse getFilmById(@PathVariable("id") long id) {
        return filmService.getById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public FilmResponse create(@RequestBody @Valid NewFilmRequest film) {
        return filmService.create(film);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public FilmResponse update(@RequestBody @Valid UpdateFilmRequest request) {
        return filmService.update(request.getId(), request);
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
    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable("id") long filmId) {
        filmService.removeFilm(filmId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/popular")
    public List<FilmResponse> getPopularFilms(@RequestParam("count") Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.getPopularFilms(count.get());
        } else {
            return filmService.getPopularFilms(10);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/director/{directorId}")
    public List<FilmResponse> getSortedFilmsOfDirector(@PathVariable long directorId, @RequestParam String sortBy) {
        return filmService.getSortedFilmsOfDirector(directorId,sortBy);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/common")
    public List<FilmResponse> commonFilms(@RequestParam long userId, @RequestParam long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
