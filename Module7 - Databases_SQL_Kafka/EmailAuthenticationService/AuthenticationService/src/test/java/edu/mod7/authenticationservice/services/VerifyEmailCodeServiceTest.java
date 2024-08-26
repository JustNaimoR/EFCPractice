package edu.mod7.authenticationservice.services;

import edu.mod7.authenticationservice.models.VerifyEmailCode;
import edu.mod7.authenticationservice.repositories.VerifyEmailCodeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VerifyEmailCodeServiceTest {
    @InjectMocks
    VerifyEmailCodeService service;
    @Mock
    VerifyEmailCodeRepository repository;


    @Test
    void save() {
        String email = "email";
        String verifyCode = "code";

        service.save(email, verifyCode);
    }

    @Test
    void getCodeByEmail() {
        String email = "email@mail.com";
        String code = "123456";

        Mockito.when(repository.findByEmail(Mockito.any())).thenReturn(Optional.of(
                new VerifyEmailCode(email, code)
        ));

        Assertions.assertEquals(code, service.getCodeByEmail(email));
    }

    @Test
    void removeByEmail() {
        service.removeByEmail("email@mail.com");
    }
}