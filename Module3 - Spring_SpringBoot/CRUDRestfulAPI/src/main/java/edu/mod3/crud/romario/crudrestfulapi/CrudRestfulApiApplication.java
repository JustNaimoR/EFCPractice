package edu.mod3.crud.romario.crudrestfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/*
todo
 - Как добавить тот самогенерирующийся файл с openAPI swagger чтобы не пришлось открывать все время в браузере
 */

@SpringBootApplication
@EnableCaching
public class CrudRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudRestfulApiApplication.class, args);
    }

}