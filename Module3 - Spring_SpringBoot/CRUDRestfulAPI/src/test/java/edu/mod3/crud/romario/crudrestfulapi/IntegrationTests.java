package edu.mod3.crud.romario.crudrestfulapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestContainersConfiguration;
import edu.mod3.crud.romario.crudrestfulapi.controllers.TodoController;
import edu.mod3.crud.romario.crudrestfulapi.dto.PageTodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapper;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.repository.ToDoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Import(TestContainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Transactional              //todo автоматический rollback в конце каждого теста. Может другие способы отменять изменения в таблице контейнера?
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
    TodoTaskDtoMapper todoTaskDTOMapper;


    // Список записей в '/sql/create_3_tasks.sql' файле
    static final List<TodoTask> listOfThree = List.of(
            new TodoTask(1, "test_title1", "test_description1"),
            new TodoTask(2, "test_title2", "test_description2"),
            new TodoTask(3, "test_title3", "test_description3")
    );
    // Одна запись в файле '/sql/create_1_task.sql'
    static final TodoTask singleTask = new TodoTask(
            0, "test_title0", "test_description0"
    );

    static final String createSingleTaskSqlFile = "/sql/create_1_task.sql";
    static final String createThreeTasksSqlFile = "/sql/create_3_tasks.sql";

    @Nested
    class createTaskTests {
        @Test
        void createTaskTest_validInput() throws Exception {
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("title")
                    .description("description")
                    .build();

            MockHttpServletResponse response = mvc.perform(
                    post("/todo/create")
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());     // task created

            int createdID = objectMapper.readTree(response.getContentAsString()).get("id").asInt();

            Assertions.assertTrue(repository.existsById(createdID));      // task exists at database
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

            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());     // correct status code

            JsonNode json = objectMapper.readTree(response.getContentAsString());

            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), json.get("status").asInt());   // correct field 'status'

            Set<String> correctList = Set.of("title - title can't be empty", "description - description can't be null");
            Set<String> responseMessagesList = new HashSet<>();

            for (JsonNode node: json.get("messages")) {
                responseMessagesList.add(node.asText());
            }

            Assertions.assertEquals(correctList, responseMessagesList);
        }
    }

    @Nested
    @Sql(createSingleTaskSqlFile)
    class updatedTests {
        @Test
        void update() throws Exception {
            TodoTaskDto newDto = TodoTaskDto.builder()
                    .title("new title")
                    .description("new description")
                    .build();

            int id = singleTask.getId();

            Assertions.assertTrue(repository.existsById(id));

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
        void update_invalid() throws Exception {
            TodoTaskDto newDto = TodoTaskDto.builder()
                    .title("")
                    .description(null)
                    .build();

            int id = singleTask.getId();
            Assertions.assertTrue(repository.existsById(id));

            mvc.perform(
                    put("/todo/update/{id}", id)
                            .content(objectMapper.writeValueAsString(newDto))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class deleteTests {
        @Test
        @Sql(createSingleTaskSqlFile)
        void simple_delete() throws Exception {
            int id = singleTask.getId();

            Assertions.assertTrue(repository.existsById(id));

            mvc.perform(
                    delete("/todo/delete/{id}", id)
            ).andExpect(
                    status().isOk()                             // correct status
            );

            Assertions.assertFalse(repository.existsById(id));         // don't exist
        }

        @Test
        void delete_notExists() throws Exception {
            int id = singleTask.getId();

            Assertions.assertFalse(repository.existsById(id));

            mvc.perform(
                    delete("/todo/delete/{id}", id)
            ).andExpect(
                    status().isBadRequest()                             // correct status
            );
        }
    }

    @Nested
    class getByIdTests {
        @Test
        @Sql(createSingleTaskSqlFile)
        void simple_getById() throws Exception {
            int id = singleTask.getId();
            TodoTaskDto expectedDto = todoTaskDTOMapper.toDto(singleTask);


            Assertions.assertTrue(repository.existsById(id));

            MockHttpServletResponse response = mvc.perform(
                    get("/todo/getById")
                            .param("id", String.valueOf(id))
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

            TodoTaskDto responseDto = objectMapper.readValue(response.getContentAsString(), TodoTaskDto.class);

            Assertions.assertEquals(expectedDto, responseDto);
        }

        @Test
        void getById_notExists() throws Exception {
            int id = -1;

            Assertions.assertFalse(repository.existsById(id));

            MockHttpServletResponse response = mvc.perform(
                    get("/todo/getById")
                            .param("id", String.valueOf(id))
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }
    }

    @Nested
    class getAllTests {
        @Test
        @Sql(createThreeTasksSqlFile)
        void getAll() throws Exception {
            PageTodoTaskDto expectedResponseDto = new PageTodoTaskDto(
                    new HashSet<>(listOfThree.stream().map(todoTaskDTOMapper::toDto).toList()),
                    0,
                    5,
                    true
            );

            MockHttpServletResponse response = mvc.perform(
                    get("/todo/all")
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

            PageTodoTaskDto responseDto = objectMapper.readValue(response.getContentAsString(), PageTodoTaskDto.class);

            Assertions.assertEquals(expectedResponseDto, responseDto);
        }
    }
}