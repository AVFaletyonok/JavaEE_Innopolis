package org.example.client.impl;

import jakarta.annotation.PostConstruct;
import org.example.client.StudentsClient;
import org.example.dto.student.StudentResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class StudentsRestClientImpl implements StudentsClient {

    private RestClient restClient;

    private static final String URL_BY_COURSE = "http://localhost:8080/api/v1/students/on-course=";

    @PostConstruct
    private void init() {
        restClient = RestClient.builder()
                .baseUrl(URL_BY_COURSE)
                .build();
    }

    @Override
    public List<StudentResponse> getStudentsByCourse(final String courseName) {
        ParameterizedTypeReference<List<StudentResponse>> type = new ParameterizedTypeReference<>() {
        };

        return restClient.get()
                .uri(courseName)
                .exchange((((clientRequest, clientResponse) -> {
                    if (clientResponse.getStatusCode().is2xxSuccessful()) {
                        return clientResponse.bodyTo(type);
                    }
                    if (clientResponse.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
//                        throw new NoSuchElementException("Data is not found"); // add a record to logs, if its not a main data
                        return List.of();
                    }
                    return List.of();
                })));
    }
}
