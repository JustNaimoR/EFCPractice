package edu.mod5.crud.romario.crudrestfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CrudRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudRestfulApiApplication.class, args);
    }

}