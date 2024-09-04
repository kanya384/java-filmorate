package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.mpa.MpaRatingResponse;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<MpaRatingResponse> findAll() {
        return mpaRatingService.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public MpaRatingResponse getGenreById(@PathVariable("id") long id) {
        return mpaRatingService.getById(id);
    }
}
