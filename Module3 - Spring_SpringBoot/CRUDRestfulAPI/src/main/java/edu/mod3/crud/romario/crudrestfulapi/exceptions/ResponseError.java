package edu.mod3.crud.romario.crudrestfulapi.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;



public record ResponseError (
        @Schema(example = "400")
        int status,
        @Schema(example = "[ \"invalid input\" ]")
        List<String> messages,
        @Schema(example = "2024-07-04T09:54:56.604283300Z")
        Instant timestamp
) {

    public ResponseError(int status, List<String> messages) {
        this(status, messages, Instant.now());
    }

    public ResponseError(List<String> messages) {
        this(HttpStatus.BAD_REQUEST.value(), messages);
    }
}