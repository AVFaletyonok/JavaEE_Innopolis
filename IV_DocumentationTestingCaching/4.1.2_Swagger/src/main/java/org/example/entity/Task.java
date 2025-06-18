package org.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Класс задача.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    /**
     * id задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор", example = "1")
    private Long id;

    /**
     * Название задачи.
     */
    @Column(columnDefinition = "LONGTEXT")
    @Schema(description = "Название задачи", example = "hometask")
    private String name;

    /**
     * Описание задачи.
     */
    @Column(columnDefinition = "LONGTEXT")
    @Schema(description = "Описание задачи", example = "Приготовить ужин.")
    private String description;

    /**
     * Дата, время создания задачи.
     */
    @Column(name = "created_at")
    @Schema(description = "Время создания / обновления задачи")
    private LocalDateTime createdAt;
}
