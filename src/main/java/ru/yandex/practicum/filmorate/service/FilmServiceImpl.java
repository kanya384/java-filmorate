package ru.yandex.practicum.filmorate.service;

import exception.BadRequestException;
import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmResponse;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SearchFilter;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private FilmStorage filmStorage;
    private GenreService genreService;
    private MpaRatingService mpaRatingService;
    private EventProcessor eventProcessor;
    private DirectorService directorService;

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

        if (film.getDirectors() != null) {
            for (Director d : film.getDirectors()) {
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

        if (film.getDirectors() != null) {
            Set<Long> createdDirectorIds = new HashSet<>();
            for (Director d : film.getDirectors()) {
                if (!createdDirectorIds.contains(d.getId())) {
                    filmStorage.addDirectorToFilm(filmResponse.getId(), d.getId());
                    createdDirectorIds.add(d.getId());
                }
            }
        }

        return filmResponse;
    }

    public FilmResponse getById(long id) {
        FilmResponse filmResponse = filmStorage.getById(id).map(FilmMapper::mapToFilmResponse)
                .orElseThrow(() -> new NotFoundException("не найден фильм с id = " + id));
        filmResponse.setDirectors(directorService.getDirectorsOfFilm(id));
        return filmResponse;
    }

    public FilmResponse update(long filmId, UpdateFilmRequest request) {
        Film oldFilm = filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("не найден фильм с id = " + filmId));

        filmStorage.clearGenresOfFilm(filmId);
        if (request.hasGenres()) {
            Set<Long> createdGenreIds = new HashSet<>();
            for (var genre : request.getGenres()) {
                if (!createdGenreIds.contains(genre.getId())) {
                    filmStorage.addGenreToFilm(request.getId(), genre.getId());
                    createdGenreIds.add(genre.getId());
                }
            }
        }

        filmStorage.deleteDirectorToFilm(filmId);
        if (request.hasDirector()) {
            List<Long> newDirectorIds = request.getDirectors().stream().map(Director::getId).toList();
            for (Long newDirectorId : newDirectorIds) {
                filmStorage.addDirectorToFilm(filmId, newDirectorId);
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

    @Override
    public List<FilmResponse> getSortedFilmsOfDirector(long directorId, String sortBy) {
        directorService.findById(directorId);
        return filmStorage.getSortedFilmsOfDirector(directorId, sortBy)
                .stream()
                .map(FilmMapper::mapToFilmResponse)
                .toList();
    }

    @Override
    public List<FilmResponse> getCommonFilms(long userId, long friendId) {
        return filmStorage.getCommonFilms(userId, friendId).stream()
                .map(FilmMapper::mapToFilmResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<FilmResponse> getPopularFilmsByGenreAndByDate(int count, int genreId, int year) {
        return filmStorage.getPopularFilmsByGenreAndByDate(count, genreId, year)
                .stream()
                .map(FilmMapper::mapToFilmResponse)
                .toList();
    }

    public List<FilmResponse> search(String query, List<String> by) {
        List<Film> films;
        if (by.contains("director") && by.contains("title")) {
            films = filmStorage.search(query, SearchFilter.DIRECTOR_AND_TITLE);
        } else if (by.contains("director")) {
            films = filmStorage.search(query, SearchFilter.DIRECTOR);
        } else if (by.contains("title")) {
            films = filmStorage.search(query, SearchFilter.TITLE);
        } else {
            throw new BadRequestException("Неверно заданы фильтры поиска");
        }
        return films.stream()
                .map(FilmMapper::mapToFilmResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FilmResponse> readFilmRecommendations(long userId) {
        List<Long> userIdsWithSimilarLikes = filmStorage.readUserIdsWithIntersectionsOnFilmLikes(userId);

        List<Film> filmsLikedByUser = filmStorage.readFilmsLikedByUsers(userId);
        Set<Long> alreadyLikedFilmsIds = new HashSet<>();
        for (Film film : filmsLikedByUser) {
            alreadyLikedFilmsIds.add(film.getId());
        }

        List<Film> filmsLikedByOtherUsers = filmStorage
                .readFilmsLikedByUsers(userIdsWithSimilarLikes.stream().mapToLong(Long::longValue).toArray());

        List<FilmResponse> response = new ArrayList<>();

        for (Film film : filmsLikedByOtherUsers) {
            if (!alreadyLikedFilmsIds.contains(film.getId())) {
                response.add(FilmMapper.mapToFilmResponse(film));
            }
        }

        return response;
    }
}
