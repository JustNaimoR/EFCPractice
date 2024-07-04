package edu.mod3.crud.romario.crudrestfulapi.controllers;

import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.exceptions.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Tag(name = "TodoController - todo task controller", description = "controller for processing todo task's requests")
public interface ITodoController {

    @Operation(
            summary = "создание новой todo задачи",
            description = "создание новой задачи и возврат её id из таблицы",
            responses = {
                    @ApiResponse(
                            description = "todo задача создана",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": "15"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "неверные входные данные",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    Map<String, String> create(TodoTaskDto dto);

    @Operation(
            summary = "получение списка всех задач",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TodoTaskDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<TodoTaskDto>> getAll();

    @Operation(
            summary = "обновление todo задачи",
            parameters = @Parameter(
                    name = "id",
                    description = "id обновляемой задачи",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "id задачи не было найдено или невалидные входные данные",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    void update(int id, TodoTaskDto dto);

    @Operation(
            summary = "удаление todo задачи по id",
            parameters = @Parameter(
                    name = "id",
                    description = "id удаляемой задачи",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "id задачи не было найдено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    void delete(int id);
}