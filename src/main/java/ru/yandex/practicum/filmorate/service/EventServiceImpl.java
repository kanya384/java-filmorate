package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.event.EventResponse;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService, EventProcessor {
    EventStorage eventStorage;

    @Override
    public List<EventResponse> readEventFeedForUser(Long userId) {
        return eventStorage.readEventFeedForUser(userId).stream().map(EventMapper::mapToEventResponse).toList();
    }

    @Override
    public void add(Long entityId, Long userId, EventType eventType) {
        eventStorage.create(createEvent(entityId, userId, eventType, Operation.ADD));
    }

    @Override
    public void remove(Long entityId, Long userId, EventType eventType) {
        eventStorage.create(createEvent(entityId, userId, eventType, Operation.REMOVE));
    }

    @Override
    public void update(Long entityId, Long userId, EventType eventType) {
        eventStorage.create(createEvent(entityId, userId, eventType, Operation.UPDATE));
    }

    private Event createEvent(Long entityId, Long userId, EventType eventType, Operation operationType) {
        return Event.builder()
                .entityId(entityId)
                .eventType(eventType)
                .operation(operationType)
                .userId(userId)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
    }
}
