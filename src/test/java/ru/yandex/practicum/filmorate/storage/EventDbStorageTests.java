package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class EventDbStorageTests {
    private final EventDbStorage eventStorage;
    private final UserDbStorage userStorage;

    @Test
    public void createEvent() {
        Long userId = createUser();
        Event event = Event.builder()
                .eventType(EventType.FRIEND)
                .operation(Operation.ADD)
                .userId(userId)
                .entityId(1L)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        assertDoesNotThrow(() -> {
            eventStorage.create(event);
        });
    }

    private Long createUser() {
        Random random = new Random();
        int randomInt = random.nextInt();
        User user = User.builder()
                .name("тест")
                .email(String.format("test%d@mail.ru", randomInt))
                .login(String.format("test%d", randomInt))
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userStorage.create(user);
        return user.getId();
    }
}
