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

/**
 * Основной контроллер с endpoint-ами для взаимодействия с api tasks.
 *
 * @author Faletyonok Alexander
 * @version 1.0
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    /**
     * Репозиторий для хранения задач.
     */
    private final TaskRepository repository;

    /**
     * Приветственный endpoint.
     *
     * @return Приветствие.
     */
    @GetMapping("/home")
    public String getHome() {
        return "Welcome from spring module 3.5.4 Spring Granted Authority!";
    }

    /**
     * Endpoint для получения всех задач.
     *
     * @return Все задачи.
     */
    @GetMapping
    public List<Task> getAllTask() {
        return repository.findAll();
    }

    /**
     * Endpoint для получения конкретной задачи.
     *
     * @param id запрашиваемой задачи.
     * @return искомую задачу.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Optional<Task> task = repository.findById(id);
        return task.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(task.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint для создания новой задачи.
     *
     * @param task создаваемая задача.
     * @return созданную задачу.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setId(null);
        task.setCreatedAt(LocalDateTime.now());
        Task savedTask = repository.saveAndFlush(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    /**
     * Endpoint для обновления конкретной задача.
     *
     * @param id обновляемой задачи.
     * @param task обновляемые данные.
     * @return обновленную задачу.
     */
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

    /**
     * Endpoint для удаления конкретной задачи.
     *
     * @param id удаляемой задачи.
     * @return возвращает код noContent() в случае успеха.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
