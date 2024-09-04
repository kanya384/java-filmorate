package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.MpaRequest;
import ru.yandex.practicum.filmorate.dto.mpa.MpaRatingResponse;
import ru.yandex.practicum.filmorate.model.MpaRating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaRatingMapper {
    public static MpaRating mapToMpaRating(MpaRequest request) {
        return MpaRating.builder()
                .id(request.getId())
                .build();
    }

    public static MpaRatingResponse mapToMpaRatingResponse(MpaRating request) {
        return MpaRatingResponse.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
    }
}
