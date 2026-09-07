package com.example.taskmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;

@SpringBootTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void taskShouldBelongToUser() {

        // Arrange
        User user = new User(10L, "Anna");
        userRepository.save(user);

        Task task = new Task(
                1L,
                "Learn JPA",
                "Practice relationships");

        task.assignTo(user);

        // Act
        taskRepository.save(task);

        Task savedTask = taskRepository.findById(1L)
                .orElseThrow();

        // Assert
        assertEquals("Anna", savedTask.getUser().getName());
    }
}