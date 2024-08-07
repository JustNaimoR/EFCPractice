package edu.mod6.linkabbreviationsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*
 - добавить миграцию для alies ссылок и добавить соответствующие сущности
 	 не забыть про настройку каскада
 */

@SpringBootApplication
public class LinkAbbreviationsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkAbbreviationsServiceApplication.class, args);
	}

}