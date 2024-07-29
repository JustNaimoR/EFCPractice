package edu.mod3.crud.romario.crudrestfulapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record PageTodoTaskDto(
        @JsonProperty("items") List<TodoTaskDto> list,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean last
) { }