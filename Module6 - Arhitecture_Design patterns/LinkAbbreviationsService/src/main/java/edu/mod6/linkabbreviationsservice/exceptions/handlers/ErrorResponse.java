package edu.mod6.linkabbreviationsservice.exceptions.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record ErrorResponse(
        @JsonProperty("error_messages") List<String> errorMessages,
        @JsonProperty("timestamp") Instant timestamp
) {
    public ErrorResponse(String message, Instant timestamp) {
        this(
                Collections.singletonList(message),
                timestamp
        );
    }
}