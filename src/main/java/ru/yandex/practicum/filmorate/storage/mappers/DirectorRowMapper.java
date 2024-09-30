package ru.yandex.practicum.filmorate.storage.mappers;

import ru.yandex.practicum.filmorate.model.Director;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorRowMapper implements RowMapper<Director> {
    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director(rs.getLong("id"), rs.getString("name"));
        return director;
    }
}
