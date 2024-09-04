package ru.yandex.practicum.filmorate.storage;

import exception.InternalServerException;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    @Data
    @Builder
    private static class FilmGenre {
        private long filmId;
        private long genreId;
        private String genreName;
    }

    private static final String FIND_ALL_QUERY = "SELECT f.id, f.title, f.description, f.release_date, f.duration," +
            "f.rating_id AS mpa_id, mpa.name AS mpa_name FROM films AS f " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id ";

    private static final String INSERT_QUERY =
            "INSERT INTO films(title, description, release_date, duration, rating_id)" +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY = "SELECT f.id, f.title, f.description, f.release_date, f.duration," +
            "f.rating_id AS mpa_id, mpa.name AS mpa_name, g.id AS genre_id, g.name AS genre_name FROM films AS f " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
            "LEFT JOIN film_genre AS fg ON fg.film_id = f.id " +
            "LEFT JOIN genre AS g ON g.id = fg.genre_id " +
            "WHERE f.id = ?";

    private static final String UPDATE_QUERY = "UPDATE films " +
            "SET title = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";

    private static final String GET_TOP_POPULAR_FILMS_QUERY =
            "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
                    "mpa.name AS mpa_name, count(fl.user_id <> 0) AS likes " +
                    "FROM films AS f " +
                    "LEFT JOIN film_likes AS fl ON fl.film_id = f.id " +
                    "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
                    "GROUP BY f.id " +
                    "ORDER BY likes DESC, title " +
                    "LIMIT ?";

    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

    private static final String REMOVE_GENRE_FROM_FILM_QUERY = "DELETE FROM film_genre WHERE film_id = ?";

    private static final String ADD_LIKE_TO_FILM_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";

    private static final String REMOVE_LIKE_FROM_FILM_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film create(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null);
        film.setId(id);
        return film;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = jdbc.query(FIND_ALL_QUERY, mapper);

        findGenresForFilms(films);
        return films;
    }

    @Override
    public Optional<Film> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        return film;
    }

    @Override
    public void addGenreToFilm(Long filmId, Long genreId) {
        update(ADD_GENRE_TO_FILM_QUERY, filmId, genreId);
    }

    @Override
    public void clearGenresOfFilm(Long filmId) {
        delete(REMOVE_GENRE_FROM_FILM_QUERY, filmId);
    }

    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        update(ADD_LIKE_TO_FILM_QUERY, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        if (!delete(REMOVE_LIKE_FROM_FILM_QUERY, filmId, userId)) {
            throw new InternalServerException("Не найден лайк для удаления");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbc.query(GET_TOP_POPULAR_FILMS_QUERY, mapper, count);
        findGenresForFilms(films);
        return films;
    }

    private void findGenresForFilms(List<Film> films) {
        ArrayList<Long> filmIds = new ArrayList<>();
        for (Film film : films) {
            filmIds.add(film.getId());
        }


        List<FilmGenre> filmGenres = jdbc.query(connection -> {
            StringBuilder query = new StringBuilder();
            query.append("SELECT g.id AS genre_id, g.name AS genre_name, fg.film_id AS film_id " +
                    "FROM film_genre AS fg LEFT join genre AS g ON fg.genre_id = g.id " +
                    "WHERE film_id IN (");
            for (int i = 0; i < filmIds.size(); i++) {
                if (i == 0) {
                    query.append("?");
                    continue;
                }
                query.append(", ?");
            }
            query.append(")");

            PreparedStatement stmt = connection.prepareStatement(query.toString());

            for (int i = 0; i < filmIds.size(); i++) {
                stmt.setLong(i + 1, filmIds.get(i));
            }
            return stmt;
        }, mapFilmGenre);


        Map<Long, Film> mapFilmIdToFilm = new HashMap<>();

        for (Film film : films) {
            mapFilmIdToFilm.put(film.getId(), film);
        }

        for (FilmGenre filmGenre : filmGenres) {
            Film film = mapFilmIdToFilm.get(filmGenre.getFilmId());
            if (film.getGenres() == null) {
                film.setGenres(new ArrayList<>());
            }

            film.getGenres().add(Genre.builder()
                    .id(filmGenre.getGenreId())
                    .name(filmGenre.getGenreName())
                    .build());
        }
    }


    private final RowMapper<FilmGenre> mapFilmGenre = (ResultSet rs, int rowNum) -> FilmGenre.builder()
            .filmId(rs.getLong("film_id"))
            .genreId(rs.getLong("genre_id"))
            .genreName(rs.getString("genre_name"))
            .build();


}
