package edu.mod7.authenticationservice.exceptions.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ErrorResponse(
        @JsonProperty("error_messages") List<String> messages
) {
    public ErrorResponse(String message) {
        this(List.of(message));
    }
}