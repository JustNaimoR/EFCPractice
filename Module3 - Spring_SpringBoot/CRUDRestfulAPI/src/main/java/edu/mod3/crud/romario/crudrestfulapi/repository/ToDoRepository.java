package edu.mod3.crud.romario.crudrestfulapi.repository;

import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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

    public int update(int id, TodoTask task) {
        return jdbcTemplate.update("update todo_table set title = ?, description = ? where id = ?",
                    task.getTitle(), task.getDescription(), id);
    }

    public int remove(int id) {     // Возвращает количество измененных записей
        return jdbcTemplate.update("delete from todo_table where id = ?", id);
    }

    public boolean existsById(int id) {
        return jdbcTemplate.queryForObject("select exists (select from todo_table where id = ?)", Boolean.class, id);
    }

    public TodoTask getById(int id) {
        return jdbcTemplate.queryForObject("select * from todo_table where id = ?", new BeanPropertyRowMapper<>(TodoTask.class), id);
    }

    public int getIdByTitleAndDescription(String title, String description) {
        List<TodoTask> result = jdbcTemplate.query("select id from todo_table where title = ? and description = ?",
                new BeanPropertyRowMapper<>(TodoTask.class), title, description);

        if (result.isEmpty())
            throw new TodoTaskNotFoundException();

        return result.get(0).getId();
    }

    public TodoTask findFirst() {
        return jdbcTemplate.queryForObject("select * from todo_table limit 1", new BeanPropertyRowMapper<>(TodoTask.class));
    }
}