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
import ru.yandex.practicum.filmorate.model.Director;
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
    private DirectorService directorService;

    public List<FilmResponse> findAll() {
        return filmStorage.findAll().stream()
                .map(FilmMapper::mapToFilmResponse)
                .map((x) -> {
                    long id = x.getId();
                    x.setDirector(new HashSet<>(directorService.getDirectorsOfFilm(id)));
                    return x;
                })
                .toList();
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

        if (film.getDirector() != null) {
            for (Director d : film.getDirector()) {
                try {
                    directorService.findById(d.getId());
                } catch (Exception e) {
                    throw new BadRequestException("не найден режиссер с id = " + d.getId());
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

        if (film.getDirector() != null) {
            Set<Long> createdDirectorIds = new HashSet<>();
            for (Director d : film.getDirector()) {
                if (!createdDirectorIds.contains(d.getId())) {
                    filmStorage.addDirectorToFilm(filmResponse.getId(), d.getId());
                }
            }
        }

        return filmResponse;
    }

    public FilmResponse getById(long id) {
        FilmResponse filmResponse = filmStorage.getById(id).map(FilmMapper::mapToFilmResponse)
                .orElseThrow(() -> new NotFoundException("не найден фильм с id = " + id));
        filmResponse.setDirector(new HashSet<>(directorService.getDirectorsOfFilm(id)));
        return filmResponse;
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
        if (request.hasDirector()) {
            Set<Long> newDirectorIds = new HashSet<>();
            filmStorage.deleteDirectorToFilm(filmId);
            for (Director d : request.getDirector()) {
                if (!newDirectorIds.contains(d.getId())) {
                    filmStorage.addDirectorToFilm(filmId, d.getId());
                }
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

        log.info("лайк фильму с id = {} от пользователя с id = {} добавлен", filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {

        filmStorage.removeLikeFromFilm(filmId, userId);

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

    @Override
    public List<FilmResponse> getSortedFilmsOfDirector(long directorId, String sortBy) {
        return filmStorage.getSortedFilmsOfDirector(directorId, sortBy)
                .stream()
                .map(FilmMapper::mapToFilmResponse)
                .toList();
    }
}
