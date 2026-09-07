// package com.example.taskmanager.repository;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.stereotype.Repository;

// import com.example.taskmanager.model.Task;

// @Repository
// public class InMemoryTaskRepository implements TaskRepository {
// private final Map<Long, Task> tasks = new HashMap<>();

// public void addTask(Task task) {
// tasks.put(task.getId(), task);
// }

// public Optional<Task> findTaskById(long id) {
// return Optional.ofNullable(tasks.get(id));
// }

// public boolean deleteTask(long id) {
// Task removedTask = tasks.remove(id);

// return removedTask != null;
// }

// public List<Task> findAll() {
// return new ArrayList<>(this.tasks.values());
// }
// }
