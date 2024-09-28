package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.EventType;

public interface EventProcessor {
    void add(Long entityId, Long userId, EventType eventType);

    void remove(Long entityId, Long userId, EventType eventType);

    void update(Long entityId, Long userId, EventType eventType);
}
