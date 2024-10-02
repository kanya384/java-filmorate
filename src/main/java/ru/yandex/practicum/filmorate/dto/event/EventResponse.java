package ru.yandex.practicum.filmorate.dto.event;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EventResponse {
    private long eventId;
    private long entityId;
    private String eventType;
    private String operation;
    private long userId;
    private long timestamp;
}
