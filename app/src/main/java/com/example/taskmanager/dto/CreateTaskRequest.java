package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateTaskRequest(

        @Positive(message = "Task ID must be greater than 0") long id,

        @NotBlank(message = "Task title cannot be null or blank") String title,

        String description

) {
}