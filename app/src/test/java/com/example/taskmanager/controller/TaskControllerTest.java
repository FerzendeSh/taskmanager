package com.example.taskmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskmanager.repository.TaskRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {

        // Arrange clean test state
        taskRepository.deleteAll();
    }

    @Test
    void createTaskShouldReturn201() throws Exception {

        String json = """
                {
                    "id": 1000,
                    "title": "Learn MockMvc",
                    "description": "Test controller"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1000))
                .andExpect(jsonPath("$.title").value("Learn MockMvc"))
                .andExpect(jsonPath("$.description").value("Test controller"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void createTaskWithBlankTitleShouldReturn400() throws Exception {

        String json = """
                {
                    "id": 2000,
                    "title": "",
                    "description": "Invalid task"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Task title cannot be null or blank"));
    }

    @Test
    void getExistingTaskShouldReturn200() throws Exception {

        String json = """
                {
                    "id": 3000,
                    "title": "Get task",
                    "description": "Test GET"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/tasks/3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3000))
                .andExpect(jsonPath("$.title").value("Get task"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void getMissingTaskShouldReturn404() throws Exception {

        mockMvc.perform(
                get("/tasks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void startTaskShouldReturn200AndInProgress() throws Exception {

        String json = """
                {
                    "id": 4000,
                    "title": "Start task",
                    "description": "Test PATCH"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                patch("/tasks/4000/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void startTaskTwiceShouldReturn409() throws Exception {

        String json = """
                {
                    "id": 4001,
                    "title": "Start twice",
                    "description": "Invalid transition"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json));

        mockMvc.perform(
                patch("/tasks/4001/start"))
                .andExpect(status().isOk());

        mockMvc.perform(
                patch("/tasks/4001/start"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Only a TODO task can be started"));
    }

    @Test
    void completeTaskShouldReturn200AndDone() throws Exception {

        String json = """
                {
                    "id": 4002,
                    "title": "Start task",
                    "description": "Test PATCH"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                patch("/tasks/4002/start"))
                .andExpect(status().isOk());

        mockMvc.perform(
                patch("/tasks/4002/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void completeTodoTaskShouldReturn409() throws Exception {

        String json = """
                {
                    "id": 4102,
                    "title": "Start task",
                    "description": "Test PATCH"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                patch("/tasks/4102/complete"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Only an in-progress task can be completed"));
    }

    @Test
    void deleteExistingTaskShouldReturn204() throws Exception {

        String json = """
                {
                    "id": 4200,
                    "title": "Delete task",
                    "description": "Test DELETE"
                }
                """;

        mockMvc.perform(
                post("/tasks")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(
                delete("/tasks/4200"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMissingTaskShouldReturn404() throws Exception {

        mockMvc.perform(
                delete("/tasks/4320"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Task with ID 4320 was not found"));
    }
}
