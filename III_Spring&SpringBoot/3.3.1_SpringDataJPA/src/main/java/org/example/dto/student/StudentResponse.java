package org.example.dto.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {

    private Long id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    private String patronymic;
    @JsonProperty("birthDate")
    private LocalDate birthDate;
    private String email;

    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("coursesNames")
    private List<String> coursesNames;
}
