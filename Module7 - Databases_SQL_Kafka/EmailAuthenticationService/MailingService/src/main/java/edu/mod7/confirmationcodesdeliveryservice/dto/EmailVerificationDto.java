package edu.mod7.confirmationcodesdeliveryservice.dto;

// Dto с данными пользователя для аутентификации с кодом
public record EmailVerificationDto(
        String username,
        String email,
        String code
) {
}