package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_groups")
public class StudentGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "studentGroup", fetch = FetchType.LAZY)
    private List<StudentEntity> students;

    public void addStudent(final StudentEntity student) {
        students.add(student);
        student.setStudentGroup(this);
    }
}
