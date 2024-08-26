package edu.mod7.authenticationservice.controllers;

import edu.mod7.authenticationservice.dto.AccessTokenDto;
import edu.mod7.authenticationservice.dto.EmailAuthenticationDto;
import edu.mod7.authenticationservice.dto.EmailVerificationDto;
import edu.mod7.authenticationservice.services.EmailAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainController {
    private final EmailAuthenticationService authService;



    @PostMapping("/auth/email")         // Начальная аутентификация с помощью email
    public void emailAuth(@RequestBody EmailAuthenticationDto authDto) {
        log.info("Email authentication processing... authDto='{}'", authDto);
        authService.sendEmailVerificationCode(authDto);
    }

    @PostMapping("/auth/email-code")     // Код с почты для конечной аутентификации
    public ResponseEntity<AccessTokenDto> emailCode(@RequestBody EmailVerificationDto verifDto) {
        log.info("Verifying of the income code... verifDto='{}'", verifDto);
        return ResponseEntity.ok(
                authService.verifyCode(verifDto)
        );
    }

    @GetMapping("/welcome")             // Доступный только для авторизированных пользователей
    public ResponseEntity<String> authWelcome() {
        log.info("Some user has entered the app!");
        return ResponseEntity.ok(
                "You are authorized!"
        );
    }
}