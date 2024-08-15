package config;

import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

// Файл-конфигурации репозиториев

@Configuration
public class RepositoryLayerConfiguration {

    @Bean
    public ToDoRepository toDoRepository(
            JdbcTemplate jdbcTemplate
    ) {
        return new ToDoRepository(jdbcTemplate);
    }

}