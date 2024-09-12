package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRatingDbStorage extends BaseDbStorage<MpaRating> implements MpaRatingStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_rating";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE id = ?";

    public MpaRatingDbStorage(JdbcTemplate jdbc, RowMapper<MpaRating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<MpaRating> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<MpaRating> getById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}
