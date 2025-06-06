package org.example.dto.courses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoursesResponse {
    private Long id;
    private String name;
    @JsonProperty("isActive")
    private boolean isActive;
}
