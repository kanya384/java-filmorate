package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
class FilmDbStorageTests {
    private final FilmStorage filmStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("DELETE FROM films");
        jdbcTemplate.execute("DELETE FROM \"user\"");
    }

    @Test
    public void createFilm() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film = filmStorage.create(film);

        final Long filmId = film.getId();

        Optional<Film> filmFromBaseOptional = filmStorage.getById(filmId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("id", filmId));
    }

    @Test
    public void findAll() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(film);
        filmStorage.create(film);

        List<Film> films = filmStorage.findAll();

        assertThat(films.size()).isEqualTo(2);
    }

    @Test
    public void getById() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film = filmStorage.create(film);

        final Long filmId = film.getId();

        Optional<Film> filmFromBaseOptional = filmStorage.getById(filmId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("id", filmId));
    }

    @Test
    public void update() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film = filmStorage.create(film);

        film.setTitle("обновленный фильм");
        filmStorage.update(film);

        final Long filmId = film.getId();

        Optional<Film> filmFromBaseOptional = filmStorage.getById(filmId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("title", "обновленный фильм"));
    }

    @Test
    public void addGenreToFilm() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film = filmStorage.create(film);

        final Long filmId = film.getId();

        filmStorage.addGenreToFilm(filmId, 1L);

        Optional<Film> filmFromBaseOptional = filmStorage.getById(filmId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase.getGenres().getFirst())
                                .hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void clearGenresOfFilm() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film = filmStorage.create(film);
        filmStorage.addGenreToFilm(film.getId(), 1L);

        final Long filmId = film.getId();

        filmStorage.clearGenresOfFilm(filmId);

        Optional<Film> filmFromBaseOptional = filmStorage.getById(filmId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase.getGenres()).isEqualTo(null));
    }

    @Test
    public void getPopularFilms() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        filmStorage.create(film);

        Film film2 = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();
        film2 = filmStorage.create(film2);

        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        userDbStorage.create(user);

        filmStorage.addLikeToFilm(film2.getId(), user.getId());


        List<Film> films = filmStorage.getPopularFilms(1);
        assertThat(films.getFirst().getId()).isEqualTo(2L);
    }

}
