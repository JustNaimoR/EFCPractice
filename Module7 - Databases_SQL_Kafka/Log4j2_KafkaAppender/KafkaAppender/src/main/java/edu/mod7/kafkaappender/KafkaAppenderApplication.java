package edu.mod7.kafkaappender;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class KafkaAppenderApplication {
    private static final Logger logger = LogManager.getLogger(KafkaAppender.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaAppenderApplication.class, args);
    }

    @PostConstruct
    public void foo() {
        System.out.println("before...");
        logger.info("Hello world!");
        System.out.println("after...");
    }
}