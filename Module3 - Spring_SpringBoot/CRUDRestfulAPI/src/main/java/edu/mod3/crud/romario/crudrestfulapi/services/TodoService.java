package edu.mod3.crud.romario.crudrestfulapi.services;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapper;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.TodoTaskNotFoundException;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@CacheConfig(cacheNames="totoTasksCache")
public class TodoService {

    private TodoTaskDtoMapper todoTaskMapper;
    private ToDoRepository repository;



    @CachePut(key = "#result")          // вкладываем значение по полученному id в кеш
    public int createToDoTask(TodoTaskDto dto) {
        log.info("creating new todo task... (title = '{}')", dto.title());
        return repository.saveAndRetrieveId(todoTaskMapper.fromDto(dto));
    }

    @Cacheable(key = "#id")
    public TodoTask getById(int id) {
        log.info("getting by id...");
        if (!existsById(id))
            throw new TodoTaskNotFoundException();

        return repository.getById(id);
    }

    public List<TodoTask> getAll() {
        log.info("getting all tasks...");
        return repository.getAll();
    }

    public List<TodoTask> getAll(int pageNo, int pageSize) {
        log.info("getting all tasks with pageSize={} and pageNo={}...", pageSize, pageNo);
        return repository.getAll(pageNo, pageSize);
    }

    public boolean pageIsLast(int pageNo, int pageSize) {
        return repository.pageIsLast(pageNo, pageSize);
    }

    @CachePut(key = "#id", condition = "#root.target.existsById(#id)", value = "#task")
    public void update(int id, TodoTask task) {
        log.info("updating task with id - {}", id);

        int result = repository.update(id, task);

        if (result == 0)
            throw new TodoTaskNotFoundException();
    }

    @CacheEvict(key = "#id")
    public void delete(int id) {
        log.info("deleting task with id - {}", id);

        int result = repository.remove(id);

        if (result == 0)
            throw new TodoTaskNotFoundException();
    }

    public boolean existsById(int id) {
        log.info("checking if task with id - {} exists", id);
        return repository.existsById(id);
    }

    public int getIdByTitleAndDescription(String title, String description) {
        log.info("Getting id by title={} and description={}", title, description);
        return repository.getIdByTitleAndDescription(title, description);
    }
}