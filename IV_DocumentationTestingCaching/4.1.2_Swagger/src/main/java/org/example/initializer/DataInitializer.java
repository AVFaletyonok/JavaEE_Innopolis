package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.Task;
import org.example.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Компонент для начальной инициализации данных задач.
 *
 * @author Faletyonok Alexander
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    /**
     * Репозиторий для хранения задач.
     */
    private final TaskRepository taskRepository;

    /**
     * Метод создания в репозитории 5 начальных задач.
     */
    @PostConstruct
    public void init() {
        if (taskRepository.count() == 0) {
            Task task1 = new Task(1L, "hometask1", "Eat breakfast",
                                    LocalDateTime.of(2025, 4, 14, 7, 30));
            Task task2 = new Task(2L, "worktask1", "Work efficient",
                                    LocalDateTime.of(2025, 4, 14, 9, 00));
            Task task3 = new Task(3L, "hometask2", "Walk home",
                                    LocalDateTime.of(2025, 4, 14, 18, 00));
            Task task4 = new Task(4L, "hometask3", "Make dinner",
                                    LocalDateTime.of(2025, 4, 14, 19, 00));
            Task task5 = new Task(5L, "hometask4", "Eat dinner",
                                    LocalDateTime.of(2025, 4, 14, 19, 30));
            taskRepository.saveAndFlush(task1);
            taskRepository.saveAndFlush(task2);
            taskRepository.saveAndFlush(task3);
            taskRepository.saveAndFlush(task4);
            taskRepository.saveAndFlush(task5);
        }
    }
}
