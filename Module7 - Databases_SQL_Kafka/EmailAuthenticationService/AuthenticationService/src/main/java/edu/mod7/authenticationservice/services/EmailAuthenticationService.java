package edu.mod7.authenticationservice.services;

import edu.mod7.authenticationservice.dto.AccessTokenDto;
import edu.mod7.authenticationservice.dto.EmailAuthenticationDto;
import edu.mod7.authenticationservice.dto.EmailVerificationDto;
import edu.mod7.authenticationservice.exceptions.VerifiedCodeIsInvalidException;
import edu.mod7.authenticationservice.models.Client;
import edu.mod7.authenticationservice.security.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthenticationService {
    @Value("${kafka.topics.email-verification-topic}")
    private String EMAIL_VERIFICATION_TOPIC;

    private final KafkaProducerService kafkaProducerService;
    private final VerifyEmailCodeService verifyEmailCodeService;
    private final JWTService jwtService;
    private final ClientService clientService;



    @Transactional
    public void sendEmailVerificationCode(EmailAuthenticationDto dto) {
        String code = generateRandomCode();

        EmailVerificationDto verificationCodeDto = new EmailVerificationDto(
                dto.username(),
                dto.email(),
                code
        );

        verifyEmailCodeService.save(
                dto.email(),
                code
        );

        kafkaProducerService.sendObject(EMAIL_VERIFICATION_TOPIC, verificationCodeDto);
    }

    @Transactional
    public AccessTokenDto verifyCode(EmailVerificationDto verifDto) {
        String username = verifDto.username();
        String email = verifDto.email();
        String code = verifDto.code();

        String validCode = verifyEmailCodeService.getCodeByEmail(email);

        if (code.equals(validCode)) {
            verifyEmailCodeService.removeByEmail(email);    // Если код корректен, то удаляем его из postgres
        } else {
            throw new VerifiedCodeIsInvalidException();
        }

        log.info("Code is valid. Creating new client... verifDto={}", verifDto);

        clientService.save(
                new Client(0, username, email)
        );

        String token = jwtService.generateAccessToken(username);

        return new AccessTokenDto(token);
    }

    // Генерация кода из 6 цифр для подтверждения
    private String generateRandomCode() {
        return String.valueOf(
                new SecureRandom().nextInt(1000000)
        );
    }
}