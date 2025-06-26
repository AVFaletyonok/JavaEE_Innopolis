package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Task;
import org.example.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    // it should be in service layer, but so lectures))
    private final TaskRepository repository;

    @GetMapping("/home")
    public String getHome() {
        return "Welcome from spring module 3.5.6 Spring Json Web Token!";
    }

    @GetMapping
    public List<Task> getAllTask() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Optional<Task> task = repository.findById(id);
        return task.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(task.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setId(null);
        task.setCreatedAt(LocalDateTime.now());
        Task savedTask = repository.saveAndFlush(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task task) {
        Optional<Task> taskDB = repository.findById(id);
        if (taskDB.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        task.setId(taskDB.get().getId());
        task.setCreatedAt(LocalDateTime.now());
        repository.saveAndFlush(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
