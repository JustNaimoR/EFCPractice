package edu.mod7.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Dto для access токена пользователю
public record AccessTokenDto(
        @JsonProperty("access_token") String accessToken
) {
}
