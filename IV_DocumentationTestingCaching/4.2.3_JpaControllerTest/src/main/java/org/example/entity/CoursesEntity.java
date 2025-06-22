package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class CoursesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "course_id") //??? unique = false)
    private Long courseId;

    @OneToMany(mappedBy = "course")
    private List<ReviewEntity> reviews;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_course_fk",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
    private List<StudentEntity> students;
}
