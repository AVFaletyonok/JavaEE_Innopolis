package org.example.entity;

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
    private Long id;

    /**
     * Название задачи.
     */
    @Column(columnDefinition = "LONGTEXT")
    private String name;

    /**
     * Описание задачи.
     */
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    /**
     * Дата, время создания задачи.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
