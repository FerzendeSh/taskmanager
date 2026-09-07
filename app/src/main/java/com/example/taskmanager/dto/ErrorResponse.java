package com.example.taskmanager.dto;

public record ErrorResponse(
                int status,
                String error,
                String message) {

}
