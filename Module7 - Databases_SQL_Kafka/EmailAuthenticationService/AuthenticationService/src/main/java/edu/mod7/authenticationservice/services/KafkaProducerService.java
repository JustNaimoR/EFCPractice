package edu.mod7.authenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod7.authenticationservice.exceptions.EmailAuthenticationException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;



    public <T> void sendObject(String topic, T t) {
        try {
            log.info("Sending an object '{}' to the topic '{}'", t, topic);

            kafkaTemplate.send(topic, objectMapper.writeValueAsString(t));
        } catch (JsonProcessingException exc) {
            String errMessage = "Error during translating object to json for Kafka";

            log.error(errMessage);
            throw new EmailAuthenticationException(errMessage, exc);
        }
    }
}