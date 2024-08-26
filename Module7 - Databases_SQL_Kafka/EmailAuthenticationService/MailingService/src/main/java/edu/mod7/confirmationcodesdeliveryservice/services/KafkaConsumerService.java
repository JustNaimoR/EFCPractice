package edu.mod7.confirmationcodesdeliveryservice.services;

import edu.mod7.confirmationcodesdeliveryservice.dto.EmailVerificationDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;



    @KafkaListener(
            topics = "${kafka.topics.email-verification-topic}",
            groupId = "${kafka.email-verification-group-id}"
    )
    public void receiveEmail(EmailVerificationDto dto) {
        log.info("Запрошено новое подтверждение '{}'", dto);

        emailService.sendTextMessage(
                dto.email(),
                "verification code to " + dto.username(),
                "Hello! your verification code is " + dto.code()
        );
    }
}