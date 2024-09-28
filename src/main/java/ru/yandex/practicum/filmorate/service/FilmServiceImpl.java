package ru.yandex.practicum.filmorate.service;

import exception.BadRequestException;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.GenreRequest;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private FilmStorage filmStorage;
    private GenreService genreService;
    private MpaRatingService mpaRatingService;
    private EventProcessor eventProcessor;

    public List<FilmResponse> findAll() {
        return filmStorage.findAll().stream().map(FilmMapper::mapToFilmResponse).toList();
    }

    public FilmResponse create(NewFilmRequest film) {
        if (film.getMpa() != null) {
            try {
                mpaRatingService.getById(film.getMpa().getId());
            } catch (Exception e) {
                throw new BadRequestException("не найден mpa с id = " + film.getMpa());
            }
        }

        if (film.getGenres() != null) {
            for (var genre : film.getGenres()) {
                try {
                    genreService.getById(genre.getId());
                } catch (Exception e) {
                    throw new BadRequestException("не найден жанр с id = " + genre.getId());
                }
            }
        }

        FilmResponse filmResponse = FilmMapper.mapToFilmResponse(filmStorage.create(FilmMapper.mapToFilm(film)));

        if (film.getGenres() != null) {
            Set<Long> createdGenreIds = new HashSet<>();
            for (var genre : film.getGenres()) {
                if (!createdGenreIds.contains(genre.getId())) {
                    filmStorage.addGenreToFilm(filmResponse.getId(), genre.getId());
                    createdGenreIds.add(genre.getId());
                }
            }

            filmResponse.setGenres(film.getGenres().stream().map(GenreMapper::mapToGenreResponse).toList());
        }

        return filmResponse;
    }

    public FilmResponse getById(long id) {
        return filmStorage.getById(id).map(FilmMapper::mapToFilmResponse)
                .orElseThrow(() -> new NotFoundException("не найден фильм с id = " + id));
    }

    public FilmResponse update(long filmId, UpdateFilmRequest request) {
        Film oldFilm = filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("не найден фильм с id = " + filmId));

        filmStorage.clearGenresOfFilm(filmId);
        if (request.hasGenres()) {
            List<Long> newGenreIds = request.getGenres().stream().map(GenreRequest::getId).toList();
            for (Long newGenreId : newGenreIds) {
                filmStorage.addGenreToFilm(filmId, newGenreId);
            }
        }

        Film updatedFilm = FilmMapper.updateFilmFields(oldFilm, request);

        filmStorage.update(updatedFilm);

        log.info("обновлен фильм {}", updatedFilm);

        return FilmMapper.mapToFilmResponse(updatedFilm);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        filmStorage.addLikeToFilm(filmId, userId);

        eventProcessor.add(filmId, userId, EventType.LIKE);
        log.info("лайк фильму с id = {} от пользователя с id = {} добавлен", filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {

        filmStorage.removeLikeFromFilm(filmId, userId);

        eventProcessor.remove(filmId, userId, EventType.LIKE);
        log.info("лайк у фильма с id = {} от пользователя с id = {} удален", filmId, userId);
    }

    @Override
    public void removeFilm(Long filmId) {

        filmStorage.removeFilmById(filmId);

        log.info("фильм с id = {} удален", filmId);
    }

    @Override
    public List<FilmResponse> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count)
                .stream()
                .map(FilmMapper::mapToFilmResponse)
                .toList();
    }
}
