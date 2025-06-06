package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "start_date")
    @JsonProperty("startDate")
    private Date startDate;

    @Column(name = "is_active")
    @JsonProperty("isActive")
    private boolean isActive;

    @Column(name = "is_archived")
    @JsonProperty("isArchived")
    private boolean isArchived;
}
