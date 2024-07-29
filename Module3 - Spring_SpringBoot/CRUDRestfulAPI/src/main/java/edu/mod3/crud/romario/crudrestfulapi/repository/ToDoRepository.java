package edu.mod3.crud.romario.crudrestfulapi.repository;

import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import lombok.AllArgsConstructor;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@AllArgsConstructor
public class ToDoRepository {
    private JdbcTemplate jdbcTemplate;

    // специально с возвратом id для удобства тестирования
    public int saveAndRetrieveId(TodoTask task) {
        String insertIntoSQL = "insert into todo_table(title, description) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertIntoSQL, new String[] {"id"});
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            return ps;
        }, keyHolder);

        return (int) keyHolder.getKey();
    }

    public List<TodoTask> getAll(int pageNo, int pageSize) {
        return jdbcTemplate.query("select * from todo_table limit ? offset ?",
                new BeanPropertyRowMapper<>(TodoTask.class), pageSize, pageNo * pageSize);
    }

    public int countAll() {
        return jdbcTemplate.queryForObject("select count(*) from todo_table", Integer.class);
    }

    public void update(int id, TodoTask task) {
        jdbcTemplate.update("update todo_table set title = ?, description = ? where id = ?",
                task.getTitle(), task.getDescription(), id);
    }

    public void remove(int id) {
        jdbcTemplate.update("delete from todo_table where id = ?", id);
    }

    public boolean existsById(int id) {
        return jdbcTemplate.queryForObject("select exists (select from todo_table where id = ?)", Boolean.class, id);
    }
}