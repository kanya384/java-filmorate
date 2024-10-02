package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.event.EventResponse;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    EventStorage eventStorage;
    UserService userService;

    @Override
    public List<EventResponse> readEventFeedForUser(Long userId) {
        userService.getUserById(userId);
        return eventStorage.readEventFeedForUser(userId).stream().map(EventMapper::mapToEventResponse).toList();
    }
}
