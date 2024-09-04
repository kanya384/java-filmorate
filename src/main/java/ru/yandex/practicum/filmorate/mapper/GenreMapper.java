package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.GenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreResponse;
import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {
    public static Genre mapToGenre(GenreRequest request) {
        return Genre.builder()
                .id(request.getId())
                .build();
    }

    public static GenreResponse mapToGenreResponse(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static GenreResponse mapToGenreResponse(GenreRequest genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .build();
    }
}
