package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    Long id;

    EventType eventType;

    Operation operation;

    Long userId;

    Long entityId;

    Timestamp createdAt;
}
