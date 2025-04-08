package org.example.client.impl;

import jakarta.annotation.PostConstruct;
import org.example.client.CoursesClient;
import org.example.dto.courses.CoursesResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Profile("restTemplate")
public class CoursesRestTemplateImpl implements CoursesClient {

    private RestTemplate restTemplate;

    private static final String URL = "http://localhost:8081/api/v1/courses";

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
    }

    @Override
    public CoursesResponse getCourse(Long id) {
        return restTemplate.getForObject(URL + "/" + id, CoursesResponse.class);
    }

    @Override
    public List<CoursesResponse> getCourses() {
        return restTemplate.getForObject(URL, List.class);
    }
}
