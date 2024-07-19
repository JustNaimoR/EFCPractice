package edu.mod3.crud.romario.crudrestfulapi.controllers;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.services.TodoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
@AllArgsConstructor
public class TodoController implements ITodoController {
    private TodoService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> create(@RequestBody @Valid TodoTaskDto dto) {
        return Collections.singletonMap("id", "" + service.createToDoTask(dto));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TodoTaskDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream()
                .map(TodoTaskDto::toDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable int id, @RequestBody @Valid TodoTaskDto dto) {
        service.update(id, TodoTaskDto.fromDto(dto));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}