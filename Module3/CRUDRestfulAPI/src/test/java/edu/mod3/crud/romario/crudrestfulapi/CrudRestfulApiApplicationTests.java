package edu.mod3.crud.romario.crudrestfulapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod3.crud.romario.crudrestfulapi.controllers.TodoController;
import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.services.TodoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class IntegrationTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    TodoController controller;
    @Autowired
    TodoService service;
    @Autowired
    private ObjectMapper objectMapper;



    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

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

            assertTrue(service.existsById(createdID));      // task exists at database
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
    void updateTest() throws Exception {
        TodoTaskDto oldDto = TodoTaskDto.builder()
                .title("old title")
                .description("old description")
                .build();

        TodoTaskDto newDto = TodoTaskDto.builder()
                .title("new title")
                .description("new description")
                .build();

        int createdID = service.createToDoTask(oldDto);

        mvc.perform(
                put("/todo/update/{id}", createdID)
                        .content(objectMapper.writeValueAsString(newDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());           // status is valid

        Optional<TodoTask> opt = service.getAll().stream().filter(task -> task.getId() == createdID).findAny();

        assertFalse(opt.isEmpty());             // task still exists at the table

        TodoTask updated = opt.get();

        assertEquals(newDto.title(), updated.getTitle());           // titles are same
        assertEquals(newDto.description(), updated.getDescription());       // descriptions are same
    }

    @Test
    void deleteTest() throws Exception {
        TodoTaskDto dto = TodoTaskDto.builder()
                .title("daily")
                .description("do exercises")
                .build();

        int createdID = service.createToDoTask(dto);

        mvc.perform(
                delete("/todo/delete/{id}", createdID)
        ).andExpect(
                status().isOk()                             // correct status
        );

        assertFalse(service.existsById(createdID));         // don't exist
    }
}