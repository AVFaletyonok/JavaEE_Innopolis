package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
public class ReviewEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CoursesEntity course;
}
