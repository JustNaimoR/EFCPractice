package edu.mod6.linkabbreviationsservice.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestConfiguration
public class TestContainersConfiguration {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Bean
    public PostgreSQLContainer<?> getContainer() {
        return postgres;
    }

    @Test
    void connectionEstablished() {
        Assertions.assertTrue(postgres.isCreated());
        Assertions.assertTrue(postgres.isRunning());
    }
}