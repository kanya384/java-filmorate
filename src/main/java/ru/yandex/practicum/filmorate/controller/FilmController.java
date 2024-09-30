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
    public List<FilmResponse> getPopularFilms(@RequestParam(name = "count",defaultValue = "10") Integer count,
                                              @RequestParam(required = false,defaultValue = "0") Integer genreId,
                                              @RequestParam(required = false,defaultValue = "0") Integer year) {
        if (genreId == 0 && year == 0)
            return filmService.getPopularFilms(count);
        else
            return filmService.getPopularFilmsByGenreAndByDate(count, genreId, year);
    }

}
