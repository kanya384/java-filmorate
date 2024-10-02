package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.event.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> readEventFeedForUser(Long userId);
}
