package edu.mod7.authenticationservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@Testcontainers
@TestConfiguration
public class PostgreSQLTestContainerConfiguration {
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