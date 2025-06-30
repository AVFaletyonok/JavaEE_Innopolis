package org.example.client.impl;

import jakarta.annotation.PostConstruct;
import org.example.client.CoursesClient;
import org.example.dto.courses.CourseResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CoursesRestClientImpl implements CoursesClient {

    private RestClient restClient;

    private static final String URL = "http://localhost:8081/api/v1/courses";

    @PostConstruct
    private void init() {
        restClient = RestClient.builder()
                .baseUrl(URL)
                .build();
    }

    @Override
    public CourseResponse getCourse(Long id) {
        return restClient.get()
                .uri("/" + id)
                .retrieve()
                .body(CourseResponse.class);
    }

    @Override
    public List<CourseResponse> getCourses() {
        ParameterizedTypeReference<List<CourseResponse>> type = new ParameterizedTypeReference<>() {
        };

        return restClient.get()
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
