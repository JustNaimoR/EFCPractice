package edu.mod3.crud.romario.crudrestfulapi.repository;

import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class ToDoRepository {

    private JdbcTemplate jdbcTemplate;



    private <T> T queryForObject(String sql, Class<T> requiredType, @Nullable Object... args) throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(requiredType), args);
    }

    private <T> List<T> query(String sql, Class<T> requiredType, @Nullable Object... args) throws DataAccessException {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(requiredType), args);
    }

    private int update(String sql, @Nullable Object... args) throws DataAccessException {
        return jdbcTemplate.update(sql, args);
    }



    // специально с возвратом id для удобства тестирования
    public int saveAndRetrieveId(TodoTask task) {
        String insertIntoSQL = "insert into todo_table(title, description) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(insertIntoSQL, new String[]{"id"});
                ps.setString(1, task.getTitle());
                ps.setString(2, task.getDescription());
                return ps;
            }, keyHolder);
        } catch (DataAccessException exc) {
            log.error("Failed to save a new task. task='{}'", task);        //todo а в чем смысл перехвата, если все равно потом исключение отловится в @ControllerAdvice?
            throw exc;
        }

        return (int) keyHolder.getKey();
    }

    public List<TodoTask> getAll(int pageNo, int pageSize) {
        try {
            return query("select * from todo_table limit ? offset ?", TodoTask.class, pageSize, pageNo * pageSize);
        } catch (DataAccessException exc) {
            log.error("Exception when getting a list of tasks. pageSize={} pageNo={}", pageSize, pageNo);
            throw exc;
        }
    }

    // Проверка, что указанная страница последняя в списке
    public boolean pageIsLast(int pageNo, int pageSize) {
        try {
            return query("select * from todo_table limit ? offset ?", TodoTask.class, 1, (pageNo + 1) * pageSize)
                    .isEmpty();
        } catch (DataAccessException exc) {
            log.error("Failed to check if the page is last. pageNo='{}' pageSize='{}'", pageNo, pageSize);
            throw exc;
        }
    }

    public List<TodoTask> getAll() {
        try {
            return query("select * from todo_table", TodoTask.class);
        } catch (DataAccessException exc) {
            log.error("Failed to get all tasks");
            throw exc;
        }
    }

    // Затратная операция, сканирует всю таблицу
    public int countAll() {
        try {
            return jdbcTemplate.queryForObject("select count(*) from todo_table", Integer.class);
        } catch (DataAccessException exc) {
            log.error("Failed to count entries in the database");
            throw exc;
        }
    }

    public int update(int id, TodoTask task) {
        try {
            return update("update todo_table set title = ?, description = ? where id = ?",
                    task.getTitle(), task.getDescription(), id);
        } catch (DataAccessException exc) {
            log.error("Failed to update the task. taskId='{}' task='{}'", id, task);
            throw exc;
        }
    }

    public int remove(int id) {     // Возвращает количество измененных записей
        try {
            return update("delete from todo_table where id = ?", id);
        } catch (DataAccessException exc) {
            log.error("Failed to remove the task. id='{}'", id);
            throw exc;
        }
    }

    public boolean existsById(int id) {
        try {
            return jdbcTemplate.queryForObject("select exists (select * from todo_table where id = ?)", Boolean.class, id);
        } catch (DataAccessException exc) {
            log.error("Failed to check if a task is exists. id='{}'", id);
            throw exc;
        }
    }

    public TodoTask getById(int id) {
        try {
            return queryForObject("select * from todo_table where id = ?", TodoTask.class, id);
        } catch (EmptyResultDataAccessException exc) {
            log.error("Task wasn't found. id='{}'", id);
            throw new TodoTaskNotFoundException();
        } catch (DataAccessException exc) {
            log.error("Failed to get task by id. id='{}'", id);
            throw exc;
        }
    }

    public int getIdByTitleAndDescription(String title, String description) {
        List<TodoTask> result;

        try {
            result = query("select id from todo_table where title = ? and description = ?", TodoTask.class, title, description);
        } catch (DataAccessException exc) {
            log.error("Failed to get task by title and description. title='{}' description='{}'", title, description);
            throw exc;
        }

        if (result.isEmpty())
            throw new TodoTaskNotFoundException();

        return result.get(0).getId();
    }

    public TodoTask findFirst() {
        try {
            return queryForObject("select * from todo_table limit 1", TodoTask.class);
        } catch (EmptyResultDataAccessException exc) {
            log.error("Task wasn't found");
            throw new TodoTaskNotFoundException();
        } catch (DataAccessException exc) {
            log.error("Failed to find the first task");
            throw exc;
        }
    }
}