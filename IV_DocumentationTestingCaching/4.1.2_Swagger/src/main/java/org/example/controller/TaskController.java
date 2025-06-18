package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.entity.Task;
import org.example.repository.TaskRepository;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
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
    @Operation(summary = "Приветственная страница.", description = "Приветственная страница.")
    @GetMapping("/home")
    public String getHome() {
        return "Welcome from spring module 4.1.2 Swagger UI!";
    }

    /**
     * Endpoint для получения всех задач.
     *
     * @return Все задачи.
     */
    @Operation(summary = "Получение всех задач.", description = "Получение всех задач.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи успешно найдены."),
            @ApiResponse(responseCode = "404", description = "Список задач пуст.")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTask() {
        List<Task> tasks = repository.findAll();
        return tasks.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    /**
     * Endpoint для получения конкретной задачи.
     *
     * @param id запрашиваемой задачи.
     * @return искомую задачу.
     */
    @Operation(summary = "Получение задачи по id.", description = "Получение задачи по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача с указанным id успешно найдена."),
            @ApiResponse(responseCode = "404", description = "Задача с указанным id не найдена.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(
            @PathVariable @Parameter(name = "id", description = "id искомой задачи.", example = "1") Long id) {
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
    @Operation(summary = "Создание задачи.", description = "Создание задачи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана.")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Задача для создания.", required = true,
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Task.class),
            examples = @ExampleObject(value = "{ \"name\": \"hometask\", \"description\": \"Eat breakfast\" }")))
                                               @RequestBody Task task) {
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
    @Operation(summary = "Обновление задачи по id", description = "Обновление задачи по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача с указанным id успешно обновлена."),
            @ApiResponse(responseCode = "404", description = "Задача с указанным id не найдена.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable @Parameter(name = "id", description = "id обновляемой задачи.", example = "1") Long id,
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                   description = "Задача для обновления.", required = true,
                                                   content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = Task.class),
                                                           examples = @ExampleObject(value = "{ \"name\": \"hometask\", \"description\": \"Eat breakfast\" }")))
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
    @Operation(summary = "Удаление задачи по id.", description = "Удаление задачи по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача с указанным id успешно удалена."),
            @ApiResponse(responseCode = "404", description = "Задача с указанным id не найдена.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable @Parameter(name = "id", description = "id удаляемой задачи", example = "1") Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
