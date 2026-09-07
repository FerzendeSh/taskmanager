package com.example.taskmanager.model;

import com.example.taskmanager.exception.InvalidTaskStateException;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Task {
    @Id
    private long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Task() {
    }

    public Task(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void complete() {
        if (status != TaskStatus.IN_PROGRESS) {
            throw new InvalidTaskStateException(
                    "Only an in-progress task can be completed");
        }

        status = TaskStatus.DONE;
    }

    public void start() {
        if (status != TaskStatus.TODO) {
            throw new InvalidTaskStateException(
                    "Only a TODO task can be started");
        }
        status = TaskStatus.IN_PROGRESS;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void assignTo(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + ", Title: " + title
                + ", Description: " + description
                + ", Task status: " + status;
    }
}
