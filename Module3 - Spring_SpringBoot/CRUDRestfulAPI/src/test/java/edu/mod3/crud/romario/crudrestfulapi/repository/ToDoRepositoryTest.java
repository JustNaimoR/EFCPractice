package edu.mod3.crud.romario.crudrestfulapi.repository;

import config.RepositoryLayerConfiguration;
import config.TestContainersConfiguration;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@JdbcTest
@Import({
        TestContainersConfiguration.class,
        RepositoryLayerConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ToDoRepositoryTest {
    @Autowired
    ToDoRepository repository;

    // Список записей в '/sql/create_3_tasks.sql' файле
    private static final List<TodoTask> listOfThree = List.of(
            new TodoTask(1, "test_title1", "test_description1"),
            new TodoTask(2, "test_title2", "test_description2"),
            new TodoTask(3, "test_title3", "test_description3")
    );
    // Одна запись в файле '/sql/create_1_task.sql'
    private static final TodoTask singleTask = new TodoTask(0, "test_title0", "test_description0");



    @Test
    void saveAndRetrieveId() {
        TodoTask task = TodoTask.builder()
                .title("title")
                .description("description")
                .build();

        int id = repository.saveAndRetrieveId(task);

        task.setId(id);

        Assertions.assertTrue(repository.existsById(id));           //todo использовать тестируемые методы для проверки - вообще норм идея?
        Assertions.assertEquals(task, repository.getById(id));
    }

    @Nested
    class getAll {
        @Test
        @Sql("/sql/create_3_tasks.sql")
        void getAll_3tasks() {
            List<TodoTask> list = repository.getAll();
            Assertions.assertEquals(listOfThree, list);
        }

        @Test
        void getAll_0tasks() {
            List<TodoTask> list = repository.getAll();
            Assertions.assertEquals(Collections.emptyList(), list);
        }
    }

    @Nested
    @Sql("/sql/create_3_tasks.sql")
    class pageIsLast {
        @Test
        void pageIsLast_true() {
            boolean last = repository.pageIsLast(3, 1);     // третья последняя из набора в 3
            Assertions.assertTrue(last);
        }

        @Test
        void pageIsLast_false() {
            boolean last = repository.pageIsLast(1, 1);
            Assertions.assertFalse(last);
        }
    }

    @Nested
    @Sql("/sql/create_3_tasks.sql")
    class getAllByPages {
        @Test
        void getAll_fullPage() {
            List<TodoTask> list = repository.getAll(0, 2);
            List<TodoTask> expected = List.of(listOfThree.get(0), listOfThree.get(1));

            Assertions.assertEquals(expected, list);
        }

        @Test
        void getAll_notFullPage() {
            List<TodoTask> list = repository.getAll(1, 2);
            List<TodoTask> expected = List.of(listOfThree.get(2));

            Assertions.assertEquals(expected, list);
        }

        @Test
        void getAll_emptyPage() {
            List<TodoTask> list = repository.getAll(10, 2);  // слишком далеко
            List<TodoTask> expected = Collections.emptyList();

            Assertions.assertEquals(expected, list);
        }
    }

    @Nested
    class update {
        @Test
        @Sql("/sql/create_1_task.sql")
        void simple_update() {
            int id = singleTask.getId();

            TodoTask updated_task = TodoTask.builder()
                    .id(id)
                    .title("new_title")
                    .description("new_description")
                    .build();

            int updated = repository.update(id, updated_task);
            Assertions.assertEquals(1, updated);

            TodoTask from_db = repository.getById(id);
            Assertions.assertEquals(updated_task, from_db);
        }

        @Test
        void update_notExists() {
            int id = -1;        // не существует id

            TodoTask updated_task = TodoTask.builder()
                    .id(id)
                    .title("new_title")
                    .description("new_description")
                    .build();

            int updated = repository.update(id, updated_task);
            Assertions.assertEquals(0, updated);
        }
    }

    @Nested
    class remove {
        @Test
        @Sql("/sql/create_1_task.sql")
        void simple_remove() {
            int id = singleTask.getId();

            Assertions.assertEquals(repository.countAll(), 1);
            int removed = repository.remove(id);

            Assertions.assertEquals(1, removed);
            Assertions.assertEquals(0, repository.countAll());
        }

        @Test
        void remove_notExists() {
            int id = -1;        // doesn't exist

            Assertions.assertEquals(0, repository.countAll());
            int removed = repository.remove(id);

            Assertions.assertEquals(0, removed);
        }
    }

    @Nested
    class existsById {
        @Test
        @Sql("/sql/create_1_task.sql")
        void existsById_exists() {
            Assertions.assertTrue(repository.existsById(singleTask.getId()));
        }

        @Test
        void existsById_notExists() {
            Assertions.assertFalse(repository.existsById(singleTask.getId()));
        }
    }

    @Nested
    class getById {
        @Test
        @Sql("/sql/create_1_task.sql")
        void getById_exists() {
            int id = singleTask.getId();

            TodoTask found = repository.getById(id);
            Assertions.assertEquals(singleTask, found);
        }

        @Test
        void getById_notExists() {
            int id = -1;
            Assertions.assertThrows(TodoTaskNotFoundException.class, () -> repository.getById(id));
        }
    }

    @Nested
    class getIdByTitleAndDescription {
        @Test
        @Sql("/sql/create_1_task.sql")
        void getIdByTitleAndDescription_exists() {
            String title = singleTask.getTitle();
            String description = singleTask.getDescription();

            int id = repository.getIdByTitleAndDescription(title, description);

            Assertions.assertEquals(singleTask.getId(), id);
        }

        @Test
        void getIdByTitleAndDescription_notExists() {
            String title = "invalid";
            String description = "invalid";

            Assertions.assertThrows(TodoTaskNotFoundException.class,
                    () -> repository.getIdByTitleAndDescription(title, description));
        }
    }

    @Nested
    class findFirst {
        @Test
        @Sql("/sql/create_1_task.sql")
        void simple_findFirst() {
            TodoTask found = repository.findFirst();
            Assertions.assertEquals(singleTask, found);
        }

        @Test
        void findFirst_notFound() {
            Assertions.assertThrows(TodoTaskNotFoundException.class, () -> repository.findFirst());
        }
    }

    @Nested
    class countAll {
        @Test
        @Sql("/sql/create_3_tasks.sql")
        void simple_countAll() {
            int counted = repository.countAll();
            Assertions.assertEquals(listOfThree.size(), counted);
        }
    }
}