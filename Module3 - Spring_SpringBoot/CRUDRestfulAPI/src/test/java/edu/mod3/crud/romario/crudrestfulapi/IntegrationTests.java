package edu.mod3.crud.romario.crudrestfulapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod3.crud.romario.crudrestfulapi.config.TestContainersConfiguration;
import edu.mod3.crud.romario.crudrestfulapi.controllers.TodoController;
import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import edu.mod3.crud.romario.crudrestfulapi.services.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class IntegrationTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    TodoController controller;
    @Autowired
    ToDoRepository repository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Nested
    class createTaskTests {
        @Test
        void createTaskTest_validInput() throws Exception {
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("daily")
                    .description("do exercises")
                    .build();

            MockHttpServletResponse response = mvc.perform(
                    post("/todo/create")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());     // task created

            String responseJson = response.getContentAsString();
            int createdID = objectMapper.readTree(responseJson).get("id").asInt();

            assertTrue(repository.existsById(createdID));      // task exists at database
        }

        @Test
        void createTaskTest_invalidInput() throws Exception {
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("")          // 'title can't be empty'
                    .description(null)  // 'description can't be null'
                    .build();

            MockHttpServletResponse response = mvc.perform(
                    post("/todo/create")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());     // correct status code

            JsonNode json = objectMapper.readTree(response.getContentAsString());

            assertEquals(HttpStatus.BAD_REQUEST.value(), json.get("status").asInt());   // correct field 'status'

            assertTrue(json.has("messages"));           // json has required field

            List<String> correctList = List.of("title - title can't be empty", "description - description can't be null");
            List<String> curList = new ArrayList<>();

            for (JsonNode node: json.get("messages")) {
                curList.add(node.asText());
            }

            assertTrue(correctList.containsAll(curList));           // 'messages' содержат все требуемые сообщения об ошибках
            assertTrue(curList.containsAll(correctList));
        }
    }

    @Test
    @Sql("/testSQL/create-3-tasks.sql")
    void updateTest() throws Exception {
        TodoTaskDto newDto = TodoTaskDto.builder()
                .title("new title")
                .description("new description")
                .build();

        int id = repository.findFirst().getId();

        mvc.perform(
                put("/todo/update/{id}", id)
                        .content(objectMapper.writeValueAsString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());           // status is valid

        TodoTask updated = repository.getById(id);

        Assertions.assertEquals(newDto.description(), updated.getDescription());
        Assertions.assertEquals(newDto.title(), updated.getTitle());
    }

    @Test
    @Sql("/testSQL/create-3-tasks.sql")
    void deleteTest() throws Exception {
        int id = repository.findFirst().getId();

        mvc.perform(
                delete("/todo/delete/{id}", id)
        ).andExpect(
                status().isOk()                             // correct status
        );

        assertFalse(repository.existsById(id));         // don't exist
    }
}