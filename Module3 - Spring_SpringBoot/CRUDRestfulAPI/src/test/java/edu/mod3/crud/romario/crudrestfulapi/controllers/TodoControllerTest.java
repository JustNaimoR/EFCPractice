package edu.mod3.crud.romario.crudrestfulapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mod3.crud.romario.crudrestfulapi.dto.PageTodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.TodoTaskDto;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapper;
import edu.mod3.crud.romario.crudrestfulapi.dto.mappers.TodoTaskDtoMapperImpl;
import edu.mod3.crud.romario.crudrestfulapi.entities.TodoTask;
import edu.mod3.crud.romario.crudrestfulapi.services.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@AutoConfigureMockMvc
class TodoControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    TodoController controller;
    @Autowired
    ObjectMapper objectMapper;
    @SpyBean            //todo что-то очень сомнительно так делать... Но как по другому создать бин класса, если в тестах нельзя использовать @Bean?
    TodoTaskDtoMapperImpl todoTaskDtoMapper;

    @MockBean
    TodoService service;


    @Nested
    class create {
        @Test
        void create_valid() throws Exception {
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("title")
                    .description("description")
                    .build();
            int id = 1;

            Mockito.when(service.createToDoTask(dto)).thenReturn(id);

            MockHttpServletResponse response = mvc.perform(
                    post("/todo/create")
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

            String responseJson = response.getContentAsString();
            Assertions.assertTrue(objectMapper.readTree(responseJson).has("id"));

            Assertions.assertEquals(id, objectMapper.readTree(responseJson).get("id").asInt());
        }

        @Test
        void create_invalid() throws Exception {
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

    @Test
    void getAll_defaultPage() throws Exception {
        List<TodoTask> list = List.of(
                new TodoTask(0, "title0", "description0"),
                new TodoTask(1, "title1", "description1"),
                new TodoTask(2, "title2", "description2")
        );

        PageTodoTaskDto expectedResponsePage = new PageTodoTaskDto(
                list.stream().map(todoTaskDtoMapper::toDto).collect(Collectors.toSet()),
                0,
                5,
                true
        );

        Mockito.when(service.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(list);
        Mockito.when(service.pageIsLast(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);

        MockHttpServletResponse response = mvc.perform(
                get("/todo/all")
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        PageTodoTaskDto responsePage = objectMapper.readValue(
                response.getContentAsString(),
                PageTodoTaskDto.class
        );

        Assertions.assertEquals(expectedResponsePage, responsePage);
    }

    @Nested
    class update {
        @Test
        void simple_update() throws Exception {
            int id = 0;
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("title")
                    .description("description")
                    .build();

            MockHttpServletResponse response = mvc.perform(
                    put("/todo/update/{id}", id)
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        }

        @Test
        void update_invalid() throws Exception {
            int id = 0;
            TodoTaskDto dto = TodoTaskDto.builder()
                    .title("")          // 'title can't be empty'
                    .description(null)  // 'description can't be null'
                    .build();

            MockHttpServletResponse response = mvc.perform(
                    put("/todo/update/{id}", id)
                            .content(objectMapper.writeValueAsString(dto))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andReturn().getResponse();

            Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }
    }

    @Test
    void simple_delete() throws Exception {
        int id = 0;

        MockHttpServletResponse response = mvc.perform(
                delete("/todo/delete/{id}", id)
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void getById() throws Exception  {
        int id = 0;
        TodoTask task = TodoTask.builder()
                .id(id)
                .title("title")
                .description("description")
                .build();
        TodoTaskDto expectedResponseDto = TodoTaskDto.builder()
                .title("title")
                .description("description")
                .build();

        Mockito.when(service.getById(id)).thenReturn(task);

        MockHttpServletResponse response = mvc.perform(
                get("/todo/getById")
                        .param("id", String.valueOf(id))
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

        TodoTaskDto responseDto = objectMapper.readValue(response.getContentAsString(), TodoTaskDto.class);

        Assertions.assertEquals(expectedResponseDto, responseDto);
    }
}