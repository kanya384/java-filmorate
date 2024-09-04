package ru.yandex.practicum.filmorate.storage;

import exception.InternalServerException;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM \"user\"";

    private static final String INSERT_QUERY = "INSERT INTO \"user\"(login, email, name, birthday)" +
            "VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM \"user\" WHERE id = ?";

    private static final String UPDATE_QUERY = "UPDATE \"user\" SET login = ?, email = ?, name = ?, birthday = ? " +
            "WHERE id = ?";

    private static final String FIND_ALL_USER_FRIENDS_QUERY =
            "SELECT id, name, email, login, name, birthday " +
                    "FROM \"user\" WHERE id IN " +
                    "(SELECT friend_id FROM users_friendship where initiator_id = ? AND status = 'APPROVED');";

    private static final String FIND_ALL_COMMON_USER_FRIENDS_QUERY =
            "SELECT id, name, email, login, name, birthday FROM \"user\" " +
                    "WHERE id IN (" +
                    "SELECT friend_id AS user_id " +
                    "FROM users_friendship " +
                    "WHERE initiator_id = ? AND status = 'APPROVED') " +
                    "AND id IN (SELECT friend_id AS user_id " +
                    "FROM users_friendship " +
                    "WHERE initiator_id = ? AND status = 'APPROVED');";


    private static final String ADD_FRIEND_TO_USER_QUERY = "INSERT INTO users_friendship " +
            "(initiator_id, friend_id, status) VALUES (?, ?, ?)";

    private static final String REMOVE_USERS_FRIEND_QUERY = "DELETE FROM users_friendship " +
            "WHERE initiator_id = ? and friend_id = ?";

    private static final String UPDATE_FRIENDSHIP_STATUS = "ALTER TABLE users_friendship " +
            "SET status = ? WHERE initiator_id = ? and friend_id = ?";


    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User create(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public void addFriendToUser(long userId, long friendId) {
        update(ADD_FRIEND_TO_USER_QUERY,
                userId,
                friendId,
                FriendshipStatus.APPROVED.toString());
    }

    @Override
    public void removeFriendOfUser(long userId, long friendId) {
        try {
            update(REMOVE_USERS_FRIEND_QUERY, userId, friendId);
        } catch (InternalServerException e) {
            //do nothing
        }
    }

    @Override
    public void updateFriendshipStatus(long userId, long friendId, FriendshipStatus newStatus) {
        update(
                newStatus.toString(),
                userId,
                friendId
        );
    }

    @Override
    public List<User> getAllUsersFriends(long userId) {
        return findMany(FIND_ALL_USER_FRIENDS_QUERY, userId);
    }

    @Override
    public List<User> getCommonFriendsOfUsers(long firstUserId, long secondUserId) {
        return findMany(FIND_ALL_COMMON_USER_FRIENDS_QUERY, firstUserId, secondUserId);
    }
}
