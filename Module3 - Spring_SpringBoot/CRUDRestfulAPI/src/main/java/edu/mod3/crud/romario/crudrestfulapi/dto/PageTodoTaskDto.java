package edu.mod3.crud.romario.crudrestfulapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record PageTodoTaskDto(
        @JsonProperty("items") Set<TodoTaskDto> set,
        int pageNo,
        int pageSize,
        boolean last
) { }