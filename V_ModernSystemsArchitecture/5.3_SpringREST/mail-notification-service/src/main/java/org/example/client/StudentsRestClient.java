package org.example.client;

import jakarta.annotation.PostConstruct;
import org.example.dto.student.StudentResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class StudentsRestClient {

    private RestClient restClient;

    private static final String URL = "http://localhost:8080/api/v1/students";

    @PostConstruct
    private void init() {
        restClient = RestClient.builder()
                .baseUrl(URL)
                .build();
    }

    public List<StudentResponse> getStudents() {
        ParameterizedTypeReference<List<StudentResponse>> type = new ParameterizedTypeReference<>() {
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
