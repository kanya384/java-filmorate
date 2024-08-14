package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

abstract class BaseControllerTest {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private static ConfigurableApplicationContext ctx;

    @BeforeAll
    public static void beforeAll() {
        ctx = SpringApplication.run(FilmorateApplication.class);
    }

    @AfterAll
    public static void afterAll() {
        ctx.close();
    }

    HttpResponse<String> sendHttpRequest(URI url, String method, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().header("Content-Type", "application/json").uri(url).method(method, HttpRequest.BodyPublishers.ofString(body)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(localDate.format(dtf));
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            String dateString = jsonReader.nextString();
            if (dateString.isBlank()) {
                return null;
            }

            return LocalDate.parse(dateString, dtf);
        }
    }
}
