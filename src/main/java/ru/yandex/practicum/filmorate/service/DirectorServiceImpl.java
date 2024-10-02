package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {
    DirectorStorage directorStorage;

    public Director createDirector(Director director) {
        return directorStorage.createDirector(director);
    }

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director findById(long id) {
        return directorStorage.findById(id);
    }

    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    public void deleteDirector(long id) {
        directorStorage.deleteDirector(id);
    }

    public List<Director> getDirectorsOfFilm(long filmId) {
        return directorStorage.getDirectorsOfFilm(filmId);
    }
}

