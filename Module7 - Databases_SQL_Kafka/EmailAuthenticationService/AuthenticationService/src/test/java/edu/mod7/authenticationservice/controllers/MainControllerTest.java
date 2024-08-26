package edu.mod7.authenticationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod7.authenticationservice.dto.AccessTokenDto;
import edu.mod7.authenticationservice.dto.EmailAuthenticationDto;
import edu.mod7.authenticationservice.dto.EmailVerificationDto;
import edu.mod7.authenticationservice.security.services.JWTService;
import edu.mod7.authenticationservice.services.EmailAuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;
    @Autowired
    MainController controller;
    @MockBean
    EmailAuthenticationService authService;
    @MockBean
    JWTService jwtService;
    @MockBean
    UserDetailsService userDetailsService;



    @Test
    void emailAuth() throws Exception {
        EmailAuthenticationDto dto = new EmailAuthenticationDto(
                "username", "email"
        );

        MockHttpServletResponse response = mvc.perform(
                post("/auth/email")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());       // Корректный статус
    }

    @Test
    void emailCode() throws Exception {
        EmailVerificationDto dto = new EmailVerificationDto(
                "username", "email", "code"
        );

        Mockito.when(authService.verifyCode(Mockito.any())).thenReturn(new AccessTokenDto("token"));

        MockHttpServletResponse response = mvc.perform(
                post("/auth/email-code")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        String expected = objectMapper.writeValueAsString(new AccessTokenDto("token"));

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());       // Корректный статус
        Assertions.assertEquals(expected, response.getContentAsString());           // Корректное тело ответа
    }

    @Test
    void authWelcome() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                get("/welcome")
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("You are authorized!", response.getContentAsString());
    }
}