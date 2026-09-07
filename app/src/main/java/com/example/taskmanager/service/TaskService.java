package com.example.taskmanager.service;

import com.example.taskmanager.exception.DuplicateTaskException;
import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task completeTask(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " was not found"));
        task.complete();
        return taskRepository.save(task);
    }

    public void addTask(Task task) {

        if (task.getId() <= 0) {
            throw new InvalidTaskException("Task ID must be greater than 0");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Task title cannot be null or blank");
        }

        Optional<Task> existingTask = taskRepository.findById(task.getId());

        if (existingTask.isPresent()) {
            throw new DuplicateTaskException("A task with ID " + task.getId() + " already exists");
        }

        taskRepository.save(task);
    }

    public Task findTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task with ID " + id + " was not found"));
    }

    public void deleteTask(long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task with ID " + id + " was not found"));
        taskRepository.deleteById(id);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public String getStatusMessage(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " was not found"));

        return switch (task.getStatus()) {
            case TODO -> "Task has not been started";
            case IN_PROGRESS -> "Task is in progress";
            case DONE -> "Task is completed";
        };
    }

    public Task startTask(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " was not found"));

        task.start();

        return taskRepository.save(task);
    }
}
