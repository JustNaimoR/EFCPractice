package edu.mod7.authenticationservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod7.authenticationservice.dto.AccessTokenDto;
import edu.mod7.authenticationservice.dto.EmailAuthenticationDto;
import edu.mod7.authenticationservice.dto.EmailVerificationDto;
import edu.mod7.authenticationservice.models.Client;
import edu.mod7.authenticationservice.models.VerifyEmailCode;
import edu.mod7.authenticationservice.repositories.ClientRepository;
import edu.mod7.authenticationservice.repositories.VerifyEmailCodeRepository;
import edu.mod7.authenticationservice.security.services.JWTService;
import edu.mod7.authenticationservice.services.EmailAuthenticationService;
import edu.mod7.authenticationservice.services.KafkaProducerService;
import edu.mod7.authenticationservice.services.VerifyEmailCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreSQLTestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthenticationServiceApplicationTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    KafkaProducerService kafkaProducerService;
    @SpyBean
    EmailAuthenticationService emailAuthenticationService;
    @SpyBean
    JWTService jwtService;
    @SpyBean
    VerifyEmailCodeRepository verifyEmailCodeRepository;
    @SpyBean
    ClientRepository clientRepository;


    @Test
    void emailAuthTest() throws Exception {
        EmailAuthenticationDto dto = new EmailAuthenticationDto(
                "username", "email"
        );

        MockHttpServletResponse response = mvc.perform(
                post("/auth/email")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());   // валидный статус ответа
        Assertions.assertTrue(verifyEmailCodeRepository.findByEmail("email").isPresent());  // сохранена пара email-code
    }

    @Test
    void emailCodeTest() throws Exception {
        EmailVerificationDto dto = new EmailVerificationDto(
                "username", "email", "code"
        );

        Mockito.when(verifyEmailCodeRepository.findByEmail(Mockito.any())).thenReturn(
                Optional.of(
                        new VerifyEmailCode("email", "code")
                )
        );
        Mockito.when(clientRepository.findByUsername(Mockito.anyString())).thenReturn(
                Optional.of(
                        new Client(0, "username", "email")
                )
        );
        Mockito.doNothing().when(verifyEmailCodeRepository).deleteByEmail(Mockito.any());
        Mockito.when(jwtService.generateAccessToken(Mockito.anyString())).thenReturn("token");

        MockHttpServletResponse response = mvc.perform(
                post("/auth/email-code")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());   // valid http status

        AccessTokenDto responseDto = objectMapper.readValue(response.getContentAsString(), AccessTokenDto.class);

        Assertions.assertEquals("token", responseDto.accessToken());    // correct response token
    }

    @Test
    void authWelcomeTest_NotAuthenticated() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                get("/welcome")
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    void authWelcomeTest_authenticated() throws Exception {
        Client client = new Client(
                0, "username", "email"
        );

        String token = jwtService.generateAccessToken(client);

        Mockito.when(clientRepository.findByUsername(Mockito.anyString())).thenReturn(
                Optional.of(
                        new Client(0, "username", "email")
                )
        );

        MockHttpServletResponse response = mvc.perform(
                get("/welcome")
                        .header("Authorization", "Bearer " + token)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("You are authorized!", response.getContentAsString());
    }
}