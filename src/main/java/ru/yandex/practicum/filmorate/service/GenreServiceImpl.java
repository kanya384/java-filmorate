package ru.yandex.practicum.filmorate.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {
    private GenreStorage genreStorage;

    @Override
    public List<GenreResponse> findAll() {
        return genreStorage.findAll().stream().map(GenreMapper::mapToGenreResponse).toList();
    }

    @Override
    public GenreResponse getById(long id) {
        return genreStorage.getById(id).map(GenreMapper::mapToGenreResponse)
                .orElseThrow(() -> new NotFoundException("не найден жанр с id = " + id));
    }
}
