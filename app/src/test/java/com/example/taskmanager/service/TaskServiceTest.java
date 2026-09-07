package com.example.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.taskmanager.exception.DuplicateTaskException;
import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanager.repository.TaskRepository;

public class TaskServiceTest {

    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(this.taskRepository);
    }

    @Test
    void addTaskShouldStoreTask() {

        Task task = new Task(900, "Test task", "Test description");

        when(taskRepository.findById(900L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(task));

        taskService.addTask(task);

        Task result = taskService.findTaskById(900L);

        assertEquals(900, result.getId());
        assertEquals(task, result);
    }

    @Test
    void addingDuplicateTaskShouldThrowException() {

        // Arrange
        Task existingTask = new Task(900, "Existing task", "Already stored");

        when(taskRepository.findById(900L))
                .thenReturn(Optional.of(existingTask));

        // Act + Assert
        assertThrows(DuplicateTaskException.class, () -> taskService.addTask(
                new Task(900, "Duplicate task", "Same ID")));

    }

    @Test
    void addingBlankTitleShouldThrowException() {

        // Arrange
        Task task = new Task(900, " ", "Practice Java");

        // Act + Assert
        assertThrows(InvalidTaskException.class, () -> taskService.addTask(task));

    }

    @Test
    void addingNullTitleShouldThrowException() {

        // Arrange
        Task task = new Task(900, null, "Practice Java");

        // Act + Assert
        assertThrows(InvalidTaskException.class, () -> taskService.addTask(task));

    }

    @Test
    void addingIDZeroShouldThrowException() {

        // Arrange
        Task task = new Task(0, "Learn Java", "Practice Java");

        // Act + Assert
        assertThrows(InvalidTaskException.class, () -> taskService.addTask(task));

    }

    @Test
    void startingTodoToInProgress() {

        // Arrange
        // Create the task that the repository should return
        Task task = new Task(1, "Study Java", "Practice JPA");

        // Tell Mockito what to return when the service searches for task 1
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Tell Mockito what to return when the service saves the updated task
        when(taskRepository.save(task))
                .thenReturn(task);

        // Act
        // Call the method that we actually want to test
        Task result = taskService.startTask(1L);

        // Assert
        // Verify that the business operation changed TODO -> IN_PROGRESS
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void startTaskShouldThrowExceptionWhenTaskDoesNotExist() {
        // Act + Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.startTask(2));
    }

    @Test
    void completeTaskShouldChangeInProgressTaskToDone() {

        // Arrange
        Task task = new Task(2, "Study Java", "Practice JPA");
        task.start();

        when(taskRepository.findById(2L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        // Act
        Task result = taskService.completeTask(2L);

        // Assert
        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    void completeTaskShouldThrowExceptionWhenTaskDoesNotExist() {
        // Act + Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.completeTask(2));
    }

    @Test
    void deleteTaskTest() {

        // Arrange
        Task task = new Task(3, "Delete me", "Test");

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(3L);

        // Assert
        verify(taskRepository).deleteById(3L);
    }

    @Test
    void deleteTaskShouldReturnFalseWhenTaskDoesNotExist() {

        // Act + Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(999));
    }

    @Test
    void findAllTasksShouldReturnAllTasks() {

        // Arrange
        Task task1 = new Task(1, "Task 1", "Description 1");
        Task task2 = new Task(2, "Task 2", "Description 2");
        Task task3 = new Task(3, "Task 3", "Description 3");

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2, task3));

        // Act
        List<Task> tasks = taskService.findAllTasks();

        // Assert
        assertEquals(3, tasks.size());
    }

    @Test
    void getStatusMessageShouldReturnTodoMessage() {

        // Arrange
        Task task = new Task(1, "Task", "Description");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        String result = taskService.getStatusMessage(1L);

        // Assert
        assertEquals("Task has not been started", result);
    }

    @Test
    void getStatusMessageShouldReturnInProgressMessage() {

        // Arrange
        Task task = new Task(2, "Task", "Description");
        task.start();

        when(taskRepository.findById(2L))
                .thenReturn(Optional.of(task));

        // Act
        String result = taskService.getStatusMessage(2L);

        // Assert
        assertEquals("Task is in progress", result);
    }

    @Test
    void getStatusMessageShouldReturnDoneMessage() {

        // Arrange
        Task task = new Task(3, "Task", "Description");
        task.start();
        task.complete();

        when(taskRepository.findById(3L))
                .thenReturn(Optional.of(task));

        // Act
        String result = taskService.getStatusMessage(3L);

        // Assert
        assertEquals("Task is completed", result);
    }

    @Test
    void getStatusMessageShouldThrowExceptionWhenTaskDoesNotExist() {
        // Act + Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getStatusMessage(1));
    }

    @Test
    void findTaskByIdShouldReturnTask() {

        Task task = new Task(
                1,
                "Study Java",
                "Learn Mockito");

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.findTaskById(1L);

        assertEquals(task, result);
    }

}
