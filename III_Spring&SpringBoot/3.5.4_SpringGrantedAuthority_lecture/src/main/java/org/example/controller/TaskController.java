package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Task;
import org.example.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/taskl3b")
@RequiredArgsConstructor
public class TaskController {

    // it should be in service layer, but so lectures))
    private final TaskRepository repository;

    @GetMapping
    public List<Task> getAllTask() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Optional<Task> task = repository.findById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = repository.saveAndFlush(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                           @RequestBody Task task) {
        Optional<Task> taskDB = repository.findById(id);
        if (taskDB.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Task updated = taskDB.get();
        updated.setEi(task.getEi());
        updated.setStatusTime(task.getStatusTime());
        updated.setDuration(task.getDuration());
        updated.setStatus(task.getStatus());
        updated.setGroupName(task.getGroupName());
        updated.setAssignment(task.getAssignment());
        repository.saveAndFlush(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
