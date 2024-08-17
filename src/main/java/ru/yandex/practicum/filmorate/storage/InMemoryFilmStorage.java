package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film getById(long id) {
        return films.get(id);
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values()
                .stream()
                .sorted((first, second) -> {

                    int a = 0;
                    int b = 0;
                    if (first.getLikes() != null) {
                        a = first.getLikes().size();
                    }

                    if (second.getLikes() != null) {
                        b = second.getLikes().size();
                    }

                    return b - a;

                })
                .limit(count)
                .toList();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
