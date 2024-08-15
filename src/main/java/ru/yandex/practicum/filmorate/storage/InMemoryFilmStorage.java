package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        return films.put(getNextId(), film);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getById(long id) {
        if (films.containsKey(id)) {
            throw new RuntimeException("not founded");
        }

        return films.get(id);
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            throw new RuntimeException("nothing to update");
        }

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return films.values()
                .stream()
                .sorted()
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
