package com.example.taskmanager.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.taskmanager.exception.InvalidTaskStateException;

class TaskTest {

    @Test
    void newTaskShouldHaveTodoStatus() {
        // Arrange
        Task task = new Task(199, "Test", "Test description");

        // Act
        TaskStatus taskStatus = task.getStatus();

        // Assert
        assertEquals(TaskStatus.TODO, taskStatus);
    }

    @Test
    void startShouldChangeStatusToInProgress() {

        // Arrange
        Task task = new Task(199, "Test", "Test description");

        // Act
        task.start();

        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void completeShouldChangeStatusToDone() {

        // Arrange
        Task task = new Task(199, "Test", "Test description");
        task.start();

        // Act
        task.complete();

        // Assert
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    void completingTodoTaskShouldThrowException() {
        // Arrange
        Task task = new Task(199, "Test", "Test description");

        // Act + Assert
        assertThrows(InvalidTaskStateException.class, () -> task.complete());

    }

    @Test
    void completingDoneTaskShouldThrowException() {
        // Arrange
        Task task = new Task(199, "Test", "Test description");
        task.start();
        task.complete();

        // Act + Assert
        assertThrows(InvalidTaskStateException.class, () -> task.complete());

    }

    @Test
    void startingDoneTaskShouldThrowException() {
        // Arrange
        Task task = new Task(199, "Test", "Test description");
        task.start();
        task.complete();

        // Act + Assert
        assertThrows(InvalidTaskStateException.class, () -> task.start());

    }

    @Test
    void startShouldThrowExceptionWhenTaskIsInProgress() {
        // Arrange
        Task task = new Task(199, "Test", "Test description");
        task.start();

        // Act + Assert
        assertThrows(InvalidTaskStateException.class, () -> task.start());

    }

}
