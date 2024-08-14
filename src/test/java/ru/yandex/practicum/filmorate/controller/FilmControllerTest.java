package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest extends BaseControllerTest {

    @Test
    public void shoulCreateFilm() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name("звездные войны")
                .description("скрытая угроза")
                .releaseDate(LocalDate.now())
                .duration(1).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "POST", filmJson);

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shoulNotCreateFilmWithEmptyName() throws IOException, InterruptedException {
        Film film = Film.builder()
                .description("скрытая угроза")
                .releaseDate(LocalDate.now())
                .duration(1).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "POST", filmJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shoulNotCreateFilmWithTooOldReleaseDate() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name("звездные войны")
                .description("скрытая угроза")
                .releaseDate(LocalDate.of(1700, 1, 1))
                .duration(1).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "POST", filmJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shoulNotCreateFilmWithLongDescription() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name("звездные войны")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore" +
                        "dolore magna aliqua. Ut enim ad minim veniam, " +
                        "quis nostrud exercitation ullamco laboris nisi ut aliquip ex " +
                        "ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit")
                .releaseDate(LocalDate.now())
                .duration(1).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "POST", filmJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shoulNotCreateFilmWithNegativeDuration() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name("звездные войны")
                .description("скрытая угроза")
                .releaseDate(LocalDate.now())
                .duration(-11).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "POST", filmJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shoulUpdateExistingFilm() throws IOException, InterruptedException {
        Film film = Film.builder()
                .name("звездные войны")
                .description("скрытая угроза")
                .releaseDate(LocalDate.now())
                .duration(1).build();


        URI url = URI.create("http://localhost:8080/films");
        sendHttpRequest(url, "POST", gson.toJson(film));

        film.setId(1L);
        film.setName("new film");

        String filmJson = gson.toJson(film);

        url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "PUT", filmJson);

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shoulNotUpdateNotExistingFilm() throws IOException, InterruptedException {
        Film film = Film.builder()
                .id(3L)
                .name("звездные войны")
                .description("скрытая угроза")
                .releaseDate(LocalDate.now())
                .duration(1).build();

        String filmJson = gson.toJson(film);

        URI url = URI.create("http://localhost:8080/films");
        HttpResponse<String> response = sendHttpRequest(url, "PUT", filmJson);

        assertEquals(404, response.statusCode());
    }
}
