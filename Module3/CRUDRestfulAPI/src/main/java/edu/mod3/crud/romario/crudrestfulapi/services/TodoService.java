package edu.mod3.crud.romario.crudrestfulapi.services;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TodoService {
    private ToDoRepository repository;

    public int createToDoTask(TodoTaskDto dto) {
        log.info("creating new todo task... (title = '{}')", dto.title());
        return repository.saveAndRetrieveId(TodoTaskDto.fromDto(dto));
    }

    @Cacheable("tasks")
    public List<TodoTask> getAll() {
        log.info("getting all tasks...");
        return repository.getAll();
    }

    public void update(int id, TodoTask task) {
        log.info("updating task with id - {}", id);
        if (!existsById(id))
            throw new TodoTaskNotFoundException();

        repository.update(id, task);
    }

    public void delete(int id) {
        log.info("deleting task with id - {}", id);
        if (!existsById(id))
            throw new TodoTaskNotFoundException();

        repository.remove(id);
    }

    public boolean existsById(int id) {
        log.info("checking if task with id - {} exists", id);
        return repository.existsById(id);
    }
}