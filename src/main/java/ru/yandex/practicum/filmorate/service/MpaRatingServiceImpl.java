package ru.yandex.practicum.filmorate.service;

import exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mpa.MpaRatingResponse;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaRatingServiceImpl implements MpaRatingService {
    private MpaRatingStorage mpaRatingStorage;

    @Override
    public List<MpaRatingResponse> findAll() {
        return mpaRatingStorage.findAll().stream().map(MpaRatingMapper::mapToMpaRatingResponse).toList();
    }

    @Override
    public MpaRatingResponse getById(long id) {
        return mpaRatingStorage.getById(id).map(MpaRatingMapper::mapToMpaRatingResponse)
                .orElseThrow(() -> new NotFoundException("не найден mpa с id = " + id));
    }
}
