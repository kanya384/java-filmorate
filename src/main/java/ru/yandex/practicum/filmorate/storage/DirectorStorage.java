package ru.yandex.practicum.filmorate.storage;

import exception.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Component
public class DirectorStorage extends BaseDbStorage<Director> {

    public DirectorStorage(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Director createDirector(Director director) {
        long id = insert("INSERT INTO directors(name) VALUES (?)", director.getName());
        director.setId(id);
        return director;
    }

    public List<Director> getAll() {
        return findMany("SELECT * FROM directors");
    }

    public Director findById(long id) {
        return findOne("SELECT * FROM directors WHERE id = ?", id).orElseThrow(() -> new NotFoundException("Неправильный id"));
    }

    public Director updateDirector(Director director) {
        findById(director.getId());
        update("UPDATE directors SET name=? WHERE id=?", director.getName(), director.getId());
        return director;
    }

    public void deleteDirector(long id) {
        delete("DELETE FROM directors WHERE id=?", id);
    }

    public List<Director> getDirectorsOfFilm(long filmId){
        return findMany("SELECT d.id,d.name FROM directors d " +
                                           "LEFT JOIN films_of_directors fod ON d.id=fod.director_id WHERE fod.film_id=?", filmId);
    }
}
