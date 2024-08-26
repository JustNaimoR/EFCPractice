package edu.mod7.authenticationservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {
    @InjectMocks
    KafkaProducerService service;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void sendObject() {
        String topic = "topic";
        Object obj = new Object();

        service.sendObject(topic, obj);
    }
}