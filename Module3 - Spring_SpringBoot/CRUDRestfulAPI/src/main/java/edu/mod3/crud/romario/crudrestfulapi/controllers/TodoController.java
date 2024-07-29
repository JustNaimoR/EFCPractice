package edu.mod3.crud.romario.crudrestfulapi.controllers;

import edu.mod3.crud.romario.crudrestfulapi.dto.PageTodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapper;
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
    private TodoTaskDtoMapper todoTaskDTOMapper;
    private TodoService service;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody @Valid TodoTaskDto dto) {
        return new ResponseEntity<>(
                Collections.singletonMap("id", "" + service.createToDoTask(dto)),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all")
    public ResponseEntity<PageTodoTaskDto> getAll(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        List<TodoTaskDto> list = service.getAll(pageNo, pageSize).stream()
                .map(todoTaskDTOMapper::toDto)
                .toList();
        int all = service.countAll();
        int totalPages = (int) Math.ceil((double) all / pageSize);

        return ResponseEntity.ok(
                new PageTodoTaskDto(
                        list, pageNo, pageSize, all, totalPages, pageNo + 1 == totalPages
                )
        );
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable int id, @RequestBody @Valid TodoTaskDto dto) {
        service.update(id, todoTaskDTOMapper.fromDto(dto));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

}