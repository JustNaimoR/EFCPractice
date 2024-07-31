package edu.mod3.crud.romario.crudrestfulapi;


import edu.mod3.crud.romario.crudrestfulapi.config.TestContainersConfiguration;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Import(TestContainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTests {
    @Autowired
    JdbcTemplate jdbcTemplate;

    ToDoRepository repository;      //todo ??? JdbcTest не загружает @Repository классы, есть вариант получше чем этот?

    @PostConstruct
    void init() {
        repository = new ToDoRepository(jdbcTemplate);
    }

    @Test
    void saveAndRetrieveIdTest() {
        TodoTask task = TodoTask.builder()
                .title("new_title")
                .description("new_description")
                .build();

        int createdID = repository.saveAndRetrieveId(task);
        task.setId(createdID);

        TodoTask fromDB = jdbcTemplate.queryForObject("select * from todo_table where id=? limit 1",
                new BeanPropertyRowMapper<>(TodoTask.class), createdID);

        Assertions.assertEquals(task, fromDB);
    }

    @Test
    @Sql("/testSQL/create-3-tasks.sql")
    void updateTest() {
        TodoTask oldTask = repository.findFirst();      // Получаем любую первую запись из БД
        int id = oldTask.getId();

        TodoTask newTask = TodoTask.builder()
                .title("new_title")
                .description("new_description")
                .id(id)
                .build();

        int changed = repository.update(id, newTask);

        Assertions.assertEquals(1, changed);      // было обновлено значение

        TodoTask taskInDB = repository.getById(id);

        Assertions.assertEquals(newTask, taskInDB);         // В БД лежит обновленный task
    }

    @Test
    @Sql("/testSQL/create-3-tasks.sql")
    void removeTest() {
        TodoTask oldTask = repository.findFirst();      // Получаем любую первую запись из БД
        int id = oldTask.getId();

        int changed = repository.remove(id);

        Assertions.assertEquals(1, changed);        // удалено 1 значение
        Assertions.assertFalse(repository.existsById(id));
    }
}