package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.event.EventResponse;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor
public class EventMapper {

    public static EventResponse mapToEventResponse(Event event) {
        return EventResponse.builder()
                .eventId(event.getId())
                .entityId(event.getEntityId())
                .eventType(event.getEventType().toString())
                .operation(event.getOperation().toString())
                .userId(event.getUserId())
                .timestamp(event.getCreatedAt().toInstant().toEpochMilli())
                .build();
    }
}
