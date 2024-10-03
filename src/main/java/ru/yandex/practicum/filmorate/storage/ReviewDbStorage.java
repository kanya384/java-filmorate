package ru.yandex.practicum.filmorate.storage;

import exception.InternalServerException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.LikeType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage extends BaseDbStorage<Review> implements ReviewStorage {

    private static final String INSERT_QUERY = """
            INSERT INTO reviews(content, is_positive, user_id, film_id)
            VALUES (?, ?, ?, ?);""";

    private static final String FIND_BY_ID_QUERY = """
            SELECT r.id, r.CONTENT, r.IS_POSITIVE, r.USER_ID, r.FILM_ID, sum(CASE
                                                                            WHEN rl.LIKE_TYPE = 'LIKE' THEN 1
                                                                            WHEN rl.LIKE_TYPE = 'DISLIKE' THEN -1
                                                                            ELSE 0 END) AS useful
            FROM REVIEWS AS r
            LEFT OUTER JOIN REVIEW_LIKES rl ON rl.REVIEW_ID = r.id
            WHERE r.id = ?
            GROUP BY r.ID;
            """;

    private static final String FIND_ALL_QUERY = """
            SELECT r.id, r.CONTENT, r.IS_POSITIVE, r.USER_ID, r.FILM_ID, sum(CASE
                                                                            WHEN rl.LIKE_TYPE = 'LIKE' THEN 1
                                                                            WHEN rl.LIKE_TYPE = 'DISLIKE' THEN -1
                                                                            ELSE 0 END) AS useful
            FROM REVIEWS AS r
            LEFT OUTER JOIN REVIEW_LIKES rl ON rl.REVIEW_ID = r.id
            WHERE r.film_id = ?
            GROUP BY r.ID
            ORDER BY useful desc
            LIMIT ?;
            """;
    private static final String FIND_ALL_QUERY_WITHOUT_FILM_ID = """
            SELECT r.id, r.CONTENT, r.IS_POSITIVE, r.USER_ID, r.FILM_ID, sum(CASE
                                                                            WHEN rl.LIKE_TYPE = 'LIKE' THEN 1
                                                                            WHEN rl.LIKE_TYPE = 'DISLIKE' THEN -1
                                                                            ELSE 0 END) AS useful
            FROM REVIEWS AS r
            LEFT OUTER JOIN REVIEW_LIKES rl ON rl.REVIEW_ID = r.id
            GROUP BY r.ID
            ORDER BY useful desc
            LIMIT ?;
            """;

    private static final String UPDATE_QUERY = "UPDATE reviews " +
            "SET content = ?, is_positive = ? WHERE id = ?";

    private static final String INSERT_LIKE_DISLIKE_QUERY = "MERGE INTO REVIEW_LIKES (REVIEW_ID, USER_ID, LIKE_TYPE) " +
            "VALUES(?, ?, ?);";

    private static final String REMOVE_REVIEW_QUERY = "DELETE FROM REVIEWS " +
            "WHERE id = ?";

    private static final String REMOVE_LIKE_FROM_REVIEW_QUERY = "DELETE FROM REVIEW_LIKES " +
            "WHERE review_id = ? AND user_id = ? AND like_type = ?";

    public ReviewDbStorage(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review create(Review review) {
        long id = insert(
                INSERT_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        review.setId(id);
        return review;
    }

    @Override
    public List<Review> findAll(long filmId, int count) {
        return jdbc.query(FIND_ALL_QUERY, mapper, filmId, count);
    }

    @Override
    public List<Review> findAll(int count) {
        return jdbc.query(FIND_ALL_QUERY_WITHOUT_FILM_ID, mapper, count);
    }

    @Override
    public Optional<Review> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Review update(Review review) {
        update(
                UPDATE_QUERY,
                review.getContent(),
                review.getIsPositive(),
                review.getId()
        );

        return review;
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (!delete(REMOVE_REVIEW_QUERY, reviewId)) {
            throw new InternalServerException("Не найден отзыв для удаления");
        }
    }

    @Override
    public void insertLikeDislikeToReview(Long reviewId, Long userId, LikeType likeType) {
        update(
                INSERT_LIKE_DISLIKE_QUERY,
                reviewId,
                userId,
                likeType.toString()
        );
    }

    @Override
    public void removeLikeFromReview(Long reviewId, Long userId, LikeType likeType) {
        if (!delete(REMOVE_LIKE_FROM_REVIEW_QUERY, reviewId, userId, likeType.toString())) {
            throw new InternalServerException("Не найден лайк/дизлайк для удаления");
        }
    }
}
