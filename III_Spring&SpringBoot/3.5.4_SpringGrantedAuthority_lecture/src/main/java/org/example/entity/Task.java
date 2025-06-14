package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "taskl3b", schema = "spark")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ei")
    private Double ei;

    @Column(name = "status_time")
    private LocalDateTime statusTime;

    @Column(nullable = false)
    private Double duration = 0.8;

    @Column(columnDefinition = "LONGTEXT")
    private String status;

    @Column(name = "group_name", columnDefinition = "LONGTEXT")
    private String groupName;

    @Column
    private Integer assignment = 0;
}
