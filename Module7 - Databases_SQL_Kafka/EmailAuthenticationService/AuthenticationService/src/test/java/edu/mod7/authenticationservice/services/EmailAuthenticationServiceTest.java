package edu.mod7.authenticationservice.services;

import edu.mod7.authenticationservice.dto.AccessTokenDto;
import edu.mod7.authenticationservice.dto.EmailAuthenticationDto;
import edu.mod7.authenticationservice.dto.EmailVerificationDto;
import edu.mod7.authenticationservice.security.services.JWTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailAuthenticationServiceTest {
    @InjectMocks
    EmailAuthenticationService service;
    @Mock
    KafkaProducerService kafkaProducerService;
    @Mock
    VerifyEmailCodeService verifyEmailCodeService;
    @Mock
    JWTService jwtService;



    @Test
    void sendEmailVerificationCode() {
        EmailAuthenticationDto dto = new EmailAuthenticationDto(
                "username", "email"
        );

        service.sendEmailVerificationCode(dto);
    }

    @Test
    void verifyCode() {
        EmailVerificationDto dto = new EmailVerificationDto(
                "username", "email", "code"
        );

        Mockito.when(verifyEmailCodeService.getCodeByEmail(Mockito.any())).thenReturn("code");
        Mockito.when(jwtService.generateAccessToken(Mockito.anyString())).thenReturn("token");

        AccessTokenDto expected = new AccessTokenDto("token");

        Assertions.assertEquals(expected, service.verifyCode(dto));
    }
}