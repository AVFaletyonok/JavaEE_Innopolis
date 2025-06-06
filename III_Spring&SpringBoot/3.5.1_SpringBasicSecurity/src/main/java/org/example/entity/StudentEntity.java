package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString(exclude = "studentGroup")
//@EqualsAndHashCode(exclude = "studentGroup")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonProperty("lastName")
    private String lastName;

    private String patronymic;

    @Column(name = "birth_date", nullable = false)
    @JsonProperty("birthDate")
    private LocalDate birthDate;
    private String email;

    // @ManyToOne(fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "student")
    private List<ReviewEntity> reviews;

//    @ManyToMany(fetch = FetchType.LAZY)
    @ManyToMany
    @JoinTable(name = "student_course_fk",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private List<CoursesEntity> courses;
}
