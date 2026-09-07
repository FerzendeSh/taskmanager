package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // InMemory version
    // void addTask(Task task);

    // Optional<Task> findTaskById(long id);

    // boolean deleteTask(long id);

    // List<Task> findAll();

}
