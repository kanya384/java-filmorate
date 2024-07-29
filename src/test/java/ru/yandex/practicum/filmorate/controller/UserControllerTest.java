package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void shoulCreateUser() throws IOException, InterruptedException {
        User user = User.builder()
                .name("Василий")
                .login("vasya")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1990, 6, 20)).build();

        String userJson = gson.toJson(user);

        URI url = URI.create("http://localhost:8080/users");
        HttpResponse<String> response = sendHttpRequest(url, "POST", userJson);

        assertEquals(200, response.statusCode());
    }

    @Test
    public void shoulNotCreateUserWithEmptyLogin() throws IOException, InterruptedException {
        User user = User.builder()
                .name("Василий")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1990, 6, 20)).build();

        String userJson = gson.toJson(user);

        URI url = URI.create("http://localhost:8080/users");
        HttpResponse<String> response = sendHttpRequest(url, "POST", userJson);

        assertEquals(500, response.statusCode());
    }

    @Test
    public void shoulNotCreateUserWithInvalidEmail() throws IOException, InterruptedException {
        User user = User.builder()
                .name("Василий")
                .login("vasya2")
                .email("mail.ru")
                .birthday(LocalDate.of(1990, 6, 20)).build();

        String userJson = gson.toJson(user);

        URI url = URI.create("http://localhost:8080/users");
        HttpResponse<String> response = sendHttpRequest(url, "POST", userJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shoulNotCreateUserWithInvalidBirthDate() throws IOException, InterruptedException {
        User user = User.builder()
                .name("Василий")
                .login("vasya2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(2077, 6, 20)).build();

        String userJson = gson.toJson(user);

        URI url = URI.create("http://localhost:8080/users");
        HttpResponse<String> response = sendHttpRequest(url, "POST", userJson);

        assertEquals(400, response.statusCode());
    }
}
