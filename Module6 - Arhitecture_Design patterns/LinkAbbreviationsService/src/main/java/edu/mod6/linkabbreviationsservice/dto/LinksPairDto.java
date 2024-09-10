package edu.mod6.linkabbreviationsservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Builder
public record LinksPairDto(
        @JsonProperty("short_link") String shortLink,
        @JsonProperty("src_link") String srcLink,
        @JsonProperty("allies") Set<String> allies,
        @JsonProperty("expiredIn") OffsetDateTime expiredIn
) {
}