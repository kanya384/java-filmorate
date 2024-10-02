package ru.yandex.practicum.filmorate.storage;

import exception.InternalServerException;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SearchFilter;

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

    @Data
    @Builder
    private static class FilmDirector {
        private long filmId;
        private long directorId;
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

    private static final String REMOVE_FILM_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";

    private static final String GET_COMMON_FILMS = "SELECT f.id, f.title, f.description," +
            " f.release_date, f.duration, f.rating_id AS mpa_id, " +
            "mpa.name AS mpa_name, count(fl.user_id) AS likes " +
            "FROM films AS f " +
            "INNER JOIN film_likes AS fl ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
            "WHERE fl.FILM_ID IN (SELECT f.id FROM FILMS AS f LEFT JOIN FILM_LIKES fl2 ON fl2.film_id = f.id " +
            "WHERE fl2.user_id IN (?, ?) GROUP BY f.id HAVING COUNT(DISTINCT fl2.user_id) = 2) " +
            "GROUP BY f.id " +
            "ORDER BY likes DESC";

    private static final String SEARCH_BY_DIRECTOR_QUERY = "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
            "mpa.name AS mpa_name, count(fl.user_id) AS likes " +
            "FROM films AS f " +
            "LEFT JOIN film_likes AS fl ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
            "LEFT JOIN films_of_directors AS fd ON f.id = fd.film_id " +
            "LEFT JOIN directors AS dir ON dir.id = fd.director_id " +
            "WHERE LOWER(dir.name) LIKE LOWER(?) " +
            "GROUP BY f.id " +
            "ORDER BY likes DESC";

    private static final String SEARCH_BY_DIRECTOR_AND_TITLE_QUERY = "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
            "mpa.name AS mpa_name, count(fl.user_id) AS likes " +
            "FROM films AS f " +
            "LEFT JOIN film_likes AS fl ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
            "LEFT JOIN films_of_directors AS fd ON f.id = fd.film_id " +
            "LEFT JOIN directors AS dir ON dir.id = fd.director_id " +
            "WHERE LOWER(dir.name) LIKE LOWER(?) OR LOWER(f.title) LIKE LOWER(?) " +
            "GROUP BY f.id, f.title, f.description, f.release_date, f.duration, mpa_id, mpa_name " +
            "ORDER BY likes DESC";

    private static final String SEARCH_BY_TITLE_QUERY = "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
            "mpa.name AS mpa_name, count(fl.user_id) AS likes " +
            "FROM films AS f " +
            "LEFT JOIN film_likes AS fl ON fl.film_id = f.id " +
            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
            "WHERE LOWER(f.title) LIKE LOWER(?) " +
            "GROUP BY f.id " +
            "ORDER BY likes DESC";

    private static final String READ_USERS_WITH_INTERSECTIONS_ON_LIKES = "SELECT fl1.user_id FROM FILM_LIKES fl1 " +
            "WHERE FILM_ID in (SELECT fl.film_id FROM FILM_LIKES fl WHERE user_id = ?) AND fl1.user_id != ? " +
            "GROUP BY USER_ID " +
            "ORDER BY count(*) DESC " +
            "LIMIT 10";


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
        findDirectorsForFilms(films);
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
    public void removeFilmById(Long filmId) {
        if (!delete(REMOVE_FILM_BY_ID_QUERY, filmId)) {
            throw new InternalServerException("Фильм для удаления не найден");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbc.query(GET_TOP_POPULAR_FILMS_QUERY, mapper, count);
        findGenresForFilms(films);
        findDirectorsForFilms(films);
        return films;
    }

    @Override
    public List<Film> getPopularFilmsByGenreAndByDate(int count, int genreId, int year) {
        List<Film> films = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id, " +
                "mpa.name AS mpa_name, count(fl.user_id <> 0) AS likes " +
                "FROM films f " +
                "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
                "LEFT JOIN film_genre fg ON f.id=fg.film_id " +
                "LEFT JOIN film_likes fl ON f.id=fl.film_id WHERE ");

        if ((genreId != 0) && (year != 0)) {
            query.append("fg.genre_id = ? AND EXTRACT(YEAR FROM f.release_date) = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes DESC " +
                    "LIMIT ? ");
            films.addAll(jdbc.query(query.toString(), mapper, genreId, year, count));
        }
        if ((genreId == 0) && (year != 0)) {
            query.append("EXTRACT(YEAR FROM f.release_date) = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes DESC " +
                    "LIMIT ? ");
            films.addAll(jdbc.query(query.toString(), mapper, year, count));
        }
        if ((genreId != 0) && (year == 0)) {
            query.append("fg.genre_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes DESC " +
                    "LIMIT ? ");
            films.addAll(jdbc.query(query.toString(), mapper, genreId, count));
        }
        findGenresForFilms(films);
        findDirectorsForFilms(films);
        return films;
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        List<Film> films = findMany(GET_COMMON_FILMS, userId, friendId);
        findGenresForFilms(films);
        findDirectorsForFilms(films);
        return films;
    }

    @Override
    public List<Film> search(String query, SearchFilter by) {
        String querySubstring = "%" + query + "%";
        List<Film> films;
        switch (by) {
            case TITLE -> films = findMany(SEARCH_BY_TITLE_QUERY, querySubstring);
            case DIRECTOR -> films = findMany(SEARCH_BY_DIRECTOR_QUERY, querySubstring);
            case DIRECTOR_AND_TITLE ->
                    films = findMany(SEARCH_BY_DIRECTOR_AND_TITLE_QUERY, querySubstring, querySubstring);
            default -> {
                return Collections.emptyList();
            }
        }
        findGenresForFilms(films);
        findDirectorsForFilms(films);
        return films;
    }

    @Override
    public List<Long> readUserIdsWithIntersectionsOnFilmLikes(long userId) {
        return jdbc.query(READ_USERS_WITH_INTERSECTIONS_ON_LIKES, mapId, userId, userId);
    }

    @Override
    public List<Film> readFilmsLikedByUsers(long... userIds) {
        List<Film> films = jdbc.query(connection -> {
            StringBuilder query = new StringBuilder();
            query.append(
                    "SELECT DISTINCT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id, " +
                            "mpa.name AS mpa_name " +
                            "FROM films f " +
                            "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
                            "LEFT JOIN film_genre fg ON f.id=fg.film_id " +
                            "LEFT JOIN film_likes fl ON f.id=fl.film_id WHERE fl.user_id IN (");
            for (int i = 0; i < userIds.length; i++) {
                if (i == 0) {
                    query.append("?");
                    continue;
                }
                query.append(", ?");
            }
            query.append(")");

            PreparedStatement stmt = connection.prepareStatement(query.toString());

            for (int i = 0; i < userIds.length; i++) {
                stmt.setLong(i + 1, userIds[i]);
            }
            return stmt;
        }, mapper);

        findGenresForFilms(films);
        findDirectorsForFilms(films);
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

    private void findDirectorsForFilms(List<Film> films) {
        ArrayList<Long> filmIds = new ArrayList<>();
        for (Film film : films) {
            filmIds.add(film.getId());
        }

        List<FilmDirector> filmDirectors = jdbc.query(connection -> {
            StringBuilder query = new StringBuilder();
            query.append("SELECT d.id as director_id,d.name,fod.film_id FROM directors d " +
                    "LEFT JOIN films_of_directors fod ON d.id=fod.director_id WHERE fod.film_id IN (");
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
        }, mapFilmDirector);


        Map<Long, Film> mapFilmIdToFilm = new HashMap<>();
        for (Film film : films) {
            mapFilmIdToFilm.put(film.getId(), film);
        }
        for (FilmDirector filmDirector : filmDirectors) {
            if (filmDirector == null) {
                continue;
            }
            Film film = mapFilmIdToFilm.get(filmDirector.getFilmId());
            if (film.getDirector() == null) {
                film.setDirector(new ArrayList<>());
            }

            film.getDirector().add(Director.builder()
                    .id(filmDirector.getDirectorId())
                    .build());
        }
    }

    @Override
    public void addDirectorToFilm(long filmId, long directorId) {
        update("INSERT INTO films_of_directors(film_id, director_id) VALUES (?,?)", filmId, directorId);
    }

    @Override
    public void deleteDirectorToFilm(long filmId) {
        delete("DELETE FROM films_of_directors WHERE film_id=?", filmId);
    }

    @Override
    public List<Film> getSortedFilmsOfDirector(long directorId, String sortBy) {
        String query = "";
        if (sortBy.equals("year")) {
            query = "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
                    "mpa.name AS mpa_name " +
                    "FROM films f " +
                    "LEFT JOIN films_of_directors fod ON f.id=fod.film_id " +
                    "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
                    "WHERE fod.director_id = ? " +
                    "ORDER BY f.release_date";
        }
        if (sortBy.equals("likes")) {
            query = "SELECT f.id, f.title, f.description, f.release_date, f.duration, f.rating_id AS mpa_id," +
                    "mpa.name AS mpa_name, count(fl.user_id <> 0) AS likes " +
                    "FROM films f " +
                    "LEFT JOIN films_of_directors fod ON f.id=fod.film_id " +
                    "LEFT JOIN film_likes AS fl ON fl.film_id = f.id " +
                    "LEFT JOIN mpa_rating AS mpa ON mpa.id = f.rating_id " +
                    "WHERE fod.director_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes DESC";
        }
        if (query.isBlank()) {
            throw new InternalServerException("Неправильно задан параметр сортировки");
        } else {
            List<Film> films = findMany(query, directorId);
            findGenresForFilms(films);
            findDirectorsForFilms(films);
            return films;
        }
    }

    private final RowMapper<FilmGenre> mapFilmGenre = (ResultSet rs, int rowNum) -> FilmGenre.builder()
            .filmId(rs.getLong("film_id"))
            .genreId(rs.getLong("genre_id"))
            .genreName(rs.getString("genre_name"))
            .build();

    private final RowMapper<FilmDirector> mapFilmDirector = (ResultSet rs, int rowNum) -> {
        try {
            return FilmDirector.builder()
                    .filmId(rs.getLong("film_id"))
                    .directorId(rs.getLong("director_id"))
                    .build();
        } catch (Exception e) {
            return null;
        }
    };

    private final RowMapper<Long> mapId = (ResultSet rs, int rowNum) -> {
        try {
            return rs.getLong("user_id");
        } catch (Exception e) {
            return null;
        }
    };

}
