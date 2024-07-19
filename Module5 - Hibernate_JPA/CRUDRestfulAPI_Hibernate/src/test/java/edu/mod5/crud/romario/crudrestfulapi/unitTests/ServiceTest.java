package edu.mod5.crud.romario.crudrestfulapi.unitTests;

import edu.mod5.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod5.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod5.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import edu.mod5.crud.romario.crudrestfulapi.repository.ToDoRepository;
import edu.mod5.crud.romario.crudrestfulapi.services.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private ToDoRepository repository;
    @InjectMocks
    private TodoService service;

    //todo и что тут проверять, когда методы односложные...

    @Test
    public void createTaskTest() {
        TodoTaskDto dto = new TodoTaskDto("daily", "do exercises");

        Mockito.when(repository.saveAndRetrieveId(any())).thenReturn(1);

        int expected = 1;

        assertEquals(expected, service.createToDoTask(dto));
    }

    @Test
    public void getAllTest() {
        List<TodoTask> tasks = List.of(
                TodoTask.builder().title("title1").description("description1").build(),
                TodoTask.builder().title("title2").description("description2").build(),
                TodoTask.builder().title("title3").description("description3").build()
        );

        Mockito.when(repository.getAll()).thenReturn(tasks);

        List<TodoTask> result = service.getAll();

        assertTrue(result.containsAll(tasks));
        assertTrue(tasks.containsAll(result));
    }

    @Test
    public void updateTest_idDontExists() {
        TodoTask task = TodoTask.builder().title("title").description("description").build();

        Mockito.when(repository.existsById(anyInt())).thenReturn(false);

        assertThrows(TodoTaskNotFoundException.class, () -> service.update(1, task));
    }
}