package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute("DELETE FROM \"user\"");
    }

    @Test
    public void createUser() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        userDbStorage.create(user);

        final Long userId = user.getId();

        Optional<User> userFromBaseOptional = userDbStorage.getById(userId);


        assertThat(userFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(userFromBase ->
                        assertThat(userFromBase)
                                .hasFieldOrPropertyWithValue("id", userId));
    }

    @Test
    public void findAll() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        userDbStorage.create(user);

        user.setLogin("test2");
        user.setEmail("test02@mail.ru");

        userDbStorage.create(user);

        List<User> users = userDbStorage.findAll();

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void getById() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        userDbStorage.create(user);
        final Long userId = user.getId();

        Optional<User> userFromBaseOptional = userDbStorage.getById(userId);

        assertThat(userFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(userFromBase ->
                        assertThat(userFromBase)
                                .hasFieldOrPropertyWithValue("id", userId));
    }

    @Test
    public void update() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userDbStorage.create(user);

        user.setLogin("test2");
        user.setName("test2");
        user.setEmail("test02@mail.ru");
        userDbStorage.update(user);

        final Long userId = user.getId();

        Optional<User> userFromBaseOptional = userDbStorage.getById(userId);

        assertThat(userFromBaseOptional)
                .isPresent()
                .hasValueSatisfying(userFromBase ->
                        assertThat(userFromBase)
                                .hasFieldOrPropertyWithValue("login", "test2")
                                .hasFieldOrPropertyWithValue("name", "test2")
                                .hasFieldOrPropertyWithValue("email", "test02@mail.ru"));
    }

    @Test
    public void addFriendToUser() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userDbStorage.create(user);
        final long firstUserId = user.getId();
        user.setLogin("test2");
        user.setName("test2");
        user.setEmail("test02@mail.ru");
        user = userDbStorage.create(user);

        final long secondUserId = user.getId();

        userDbStorage.addFriendToUser(firstUserId, secondUserId);

        List<User> userFriends = userDbStorage.getAllUsersFriends(firstUserId);

        assertThat(userFriends.size()).isEqualTo(1);
        assertThat(userFriends.getFirst()).hasFieldOrPropertyWithValue("id", secondUserId);
    }

    @Test
    public void removeFriendFromUser() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userDbStorage.create(user);

        final long firstUserId = user.getId();

        user.setLogin("test2");
        user.setName("test2");
        user.setEmail("test02@mail.ru");
        userDbStorage.create(user);

        final long secondUserId = user.getId();
        userDbStorage.addFriendToUser(firstUserId, secondUserId);

        List<User> userFriends = userDbStorage.getAllUsersFriends(firstUserId);

        assertThat(userFriends.size()).isEqualTo(1);

        userDbStorage.removeFriendOfUser(firstUserId, secondUserId);

        userFriends = userDbStorage.getAllUsersFriends(firstUserId);

        assertThat(userFriends.size()).isEqualTo(0);
    }

    @Test
    public void getAllUsersFriends() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userDbStorage.create(user);
        final long firstUserId = user.getId();

        user.setLogin("test2");
        user.setName("test2");
        user.setEmail("test02@mail.ru");
        userDbStorage.create(user);
        final long secondUserId = user.getId();

        userDbStorage.addFriendToUser(firstUserId, secondUserId);

        List<User> userFriends = userDbStorage.getAllUsersFriends(firstUserId);

        assertThat(userFriends.size()).isEqualTo(1);
    }

    @Test
    public void getCommonFriendsOfUsers() {
        User user = User.builder()
                .name("тест")
                .email("test01@mail.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2005, 5, 1))
                .build();
        user = userDbStorage.create(user);
        final long firstUserId = user.getId();

        user.setLogin("test2");
        user.setName("test2");
        user.setEmail("test02@mail.ru");
        userDbStorage.create(user);
        final long secondUserId = user.getId();

        user.setLogin("test3");
        user.setName("test3");
        user.setEmail("test03@mail.ru");
        userDbStorage.create(user);
        final long thirdUserId = user.getId();


        userDbStorage.addFriendToUser(firstUserId, secondUserId);
        userDbStorage.addFriendToUser(thirdUserId, secondUserId);

        List<User> userFriends = userDbStorage.getCommonFriendsOfUsers(firstUserId, thirdUserId);

        assertThat(userFriends.size()).isEqualTo(1);
        assertThat(userFriends.getFirst().getId()).isEqualTo(secondUserId);
    }
}
