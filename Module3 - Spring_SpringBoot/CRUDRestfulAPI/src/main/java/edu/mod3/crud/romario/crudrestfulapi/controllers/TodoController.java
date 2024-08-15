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
import java.util.Set;
import java.util.stream.Collectors;

/*

 - 1 пункт - обработка идет в ExceptionHandler. Разве нужно что-то еще?
 - 3 пункт - existsById() используется для того чтобы кеширований проходило только в определенном случае.
                А GetById() как-будто лучше чтобы кидало исключение если не была найдена запись, а не возвращал Optional
 - Как лучше делать настройки миграции? Нужно ли перед добавлением новых constraint'ов удалять их если существуют или это
        костыль? (Скорее спросить у Жени) Также может даже пример кинет как у них она оформляется
 - Когда ты перехватываешь исключение, например в сервисе, то как дальше лучше поступить - стоит ли вывести в лог сообщение об ошибке а потом прокинуть
        это же исключение дальше, чтобы обработать потом в @ControllerAdvice? Или как это примерно делается в методах
 - Как много тестов нужно делать? Разве всегда требуется покрывать буквально каждый метод ими?
 - При тестировании как лучше всего проверять что все верно? Можно ли использовать тестировуемые методы? например при
        тестировании сервиса использовать для проверки методы этого самого сервиса. А если нет, то как например
        проверять методы репозитория?
 - Где и как лучше всего хранить переменные окружения? Нужно ли для этого создавать отдельные файлы в проекте и добавлять их в .gitignore
        или есть вариант получше. Может через файл .env?
 - @JdbcTest не включает в себя @Repository классы. Я сделал отдельный класс-конфигурации со всеми бинами под репозитории. Хороший ли это вариант или
        можно сделать лучше? А может объявить бин репозитория прям в классе теста? Но тогда код будет захломлен... Хотелось бы иметь только тесты в нем
 - При написании разных тестов на один метод - включать их все в один @Nested класс?
 - Очистка контейнера после работы теста?
 - А что вообще в сервисном слое проверять нужно, если все методы так или иначе просто вызывают методы репозитория?
 - Как лучше всего именовать методы и классы тестов?

 */

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
        Set<TodoTaskDto> set = service.getAll(pageNo, pageSize).stream()
                .map(todoTaskDTOMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(
                new PageTodoTaskDto(
                        set, pageNo, pageSize, service.pageIsLast(pageNo, pageSize)
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

    @GetMapping("/getById")
    public ResponseEntity<TodoTaskDto> getById(
            @RequestParam("id") int id
    ) {
        return ResponseEntity.ok(
                todoTaskDTOMapper.toDto(service.getById(id))
        );
    }
}