package edu.mod7.confirmationcodesdeliveryservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod7.confirmationcodesdeliveryservice.exceptions.ConversionToDtoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Dto с данными пользователя для аутентификации с кодом
@Slf4j
public record EmailVerificationDto(
        String username,
        String email,
        String code
) {

    public static EmailVerificationDto jsonToDto(String json) {
        try {
            return new ObjectMapper()
                    .readValue(json, EmailVerificationDto.class);
        } catch (JsonProcessingException exc) {
            log.error("The received json cannot be converted to EmailVerificationDto! Received json = {}", json);

            throw new ConversionToDtoException(exc);
        }
    }

}