package edu.mod3.crud.romario.crudrestfulapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TodoTaskDto (
        @Schema(example = "daily task")
        @NotEmpty(message = "title can't be empty")
        String title,
        @Schema(example = "do some exercise")
        @NotNull(message = "description can't be null")
        String description
) { }