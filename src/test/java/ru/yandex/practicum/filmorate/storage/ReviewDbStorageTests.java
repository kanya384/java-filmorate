package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
class ReviewDbStorageTests {
    private final ReviewStorage reviewStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("DELETE FROM films");
        jdbcTemplate.execute("DELETE FROM reviews");
        jdbcTemplate.execute("DELETE FROM \"user\"");
    }

    @Test
    public void createReview() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();
        review = reviewStorage.create(review);

        final Long reviewId = review.getId();

        Optional<Review> reviewFromBaseOptional = reviewStorage.getById(reviewId);

        assertThat(reviewFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("id", reviewId));
    }

    @Test
    public void findAll() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();
        reviewStorage.create(review);
        reviewStorage.create(review);
        reviewStorage.create(review);

        List<Review> films = reviewStorage.findAll(filmId, 2);

        assertThat(films.size()).isEqualTo(2);
    }

    @Test
    public void getById() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();
        review = reviewStorage.create(review);

        final Long reviewId = review.getId();

        Optional<Review> filmFromBaseOptional = reviewStorage.getById(reviewId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("id", reviewId));
    }

    @Test
    public void update() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();

        review = reviewStorage.create(review);

        review.setContent("Обновленный коммент");
        review.setIsPositive(false);
        reviewStorage.update(review);

        final Long reviewId = review.getId();

        Optional<Review> filmFromBaseOptional = reviewStorage.getById(reviewId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("content", "Обновленный коммент")
                                .hasFieldOrPropertyWithValue("isPositive", false));
    }

    @Test
    public void addLikeToReview() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();

        review = reviewStorage.create(review);

        reviewStorage.insertLikeDislikeToReview(review.getId(), userId, LikeType.LIKE);

        final Long reviewId = review.getId();

        Optional<Review> filmFromBaseOptional = reviewStorage.getById(reviewId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("useful", 1));
    }

    @Test
    public void removeLikeFromReview() {
        long userId = createUser();
        long filmId = createFilm();
        Review review = Review.builder()
                .content("Прекрасный фильм")
                .isPositive(true)
                .userId(userId)
                .filmId(filmId)
                .build();

        review = reviewStorage.create(review);

        reviewStorage.insertLikeDislikeToReview(review.getId(), userId, LikeType.LIKE);

        reviewStorage.removeLikeFromReview(review.getId(), userId, LikeType.LIKE);

        final Long reviewId = review.getId();

        Optional<Review> filmFromBaseOptional = reviewStorage.getById(reviewId);

        assertThat(filmFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(filmFromBase ->
                        assertThat(filmFromBase)
                                .hasFieldOrPropertyWithValue("useful", 0));
    }

    private long createFilm() {
        Film film = Film.builder()
                .title("тройной форсаж")
                .description("гонки в Токио")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 5, 1))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .build())
                .build();

        filmDbStorage.create(film);

        return film.getId();
    }

    private long createUser() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        userDbStorage.create(user);
        return user.getId();
    }

}
