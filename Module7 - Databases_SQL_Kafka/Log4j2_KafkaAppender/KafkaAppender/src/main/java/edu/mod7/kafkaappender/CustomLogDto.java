package edu.mod7.kafkaappender;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.logging.LogLevel;

import java.time.Instant;

//todo кстати названия полей dto наверное нужно держать в отдельных файлах с константами? Как это организуется

public record CustomLogDto(
        @JsonProperty("timestamp") Instant instant,
        @JsonProperty("event_type") LogLevel logger,
        @JsonProperty("log_message") String message
) { }