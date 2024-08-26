package edu.mod7.authenticationservice.dto;

public record EmailAuthenticationDto(
        String username,
        String email
) {
}