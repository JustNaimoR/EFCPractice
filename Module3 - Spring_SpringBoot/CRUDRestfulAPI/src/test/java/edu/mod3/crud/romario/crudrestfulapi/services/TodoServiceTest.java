package edu.mod3.crud.romario.crudrestfulapi.services;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapper;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock
    ToDoRepository repository;
    @Mock
    TodoTaskDtoMapper todoTaskMapper;

    @InjectMocks
    TodoService service;

    @Test
    void createToDoTask() {
        TodoTaskDto dto = TodoTaskDto.builder()
                .title("title")
                .description("description")
                .build();

        Mockito.when(repository.saveAndRetrieveId(Mockito.any())).thenReturn(0);

        int result = service.createToDoTask(dto);

        Assertions.assertEquals(result, 0);
    }

    @Nested
    class getById {
        @Test
        void simple_getById() {
            int id = 0;

            Mockito.when(service.existsById(id)).thenReturn(true);
            Mockito.when(repository.getById(id)).thenReturn(new TodoTask());

            Assertions.assertNotNull(service.getById(id));
        }

        @Test
        void getById_notExist() {
            int id = 0;

            Mockito.when(service.existsById(id)).thenReturn(false);

            Assertions.assertThrows(TodoTaskNotFoundException.class, () -> service.getById(id));
        }
    }

    @Test
    void getAll() {
        Mockito.when(repository.getAll()).thenReturn(List.of());
        Assertions.assertNotNull(service.getAll());
    }

    @Test
    void getAllByPage() {
        int pageNo = 0;
        int pageSize = 1;

        Mockito.when(repository.getAll(pageNo, pageSize)).thenReturn(List.of());

        Assertions.assertNotNull(service.getAll(pageNo, pageSize));
    }

    @Test
    void pageIsLast() {
        int pageNo = 0;
        int pageSize = 2;

        Mockito.when(repository.pageIsLast(pageNo, pageSize)).thenReturn(true);

        Assertions.assertTrue(service.pageIsLast(pageNo, pageSize));
    }

    @Nested
    class update {
        @Test
        void simple_update() {
            int id = 0;
            TodoTask task = new TodoTask();

            Mockito.when(repository.update(id, task)).thenReturn(1);

            Assertions.assertDoesNotThrow(() -> service.update(id, task));
        }
        @Test
        void update_notExists() {
            int id = 0;
            TodoTask task = new TodoTask();

            Mockito.when(repository.update(id, task)).thenReturn(0);

            Assertions.assertThrows(TodoTaskNotFoundException.class, () -> service.update(id, task));
        }
    }

    @Nested
    class delete {
        @Test
        void simple_delete() {
            int id = 0;

            Mockito.when(repository.remove(id)).thenReturn(1);

            Assertions.assertDoesNotThrow(() -> service.delete(id));
        }
        @Test
        void delete_notExists() {
            int id = 0;

            Mockito.when(repository.remove(id)).thenReturn(0);

            Assertions.assertThrows(TodoTaskNotFoundException.class, () -> service.delete(id));
        }
    }

    @Test
    void existsById() {
        int id = 0;

        Mockito.when(repository.existsById(id)).thenReturn(true);

        Assertions.assertTrue(service.existsById(id));
    }

    @Test
    void getIdByTitleAndDescription() {
        String title = "title";
        String description = "description";
        int id = 0;

        Mockito.when(repository.getIdByTitleAndDescription(title, description)).thenReturn(id);

        Assertions.assertEquals(id, service.getIdByTitleAndDescription(title, description));
    }
}