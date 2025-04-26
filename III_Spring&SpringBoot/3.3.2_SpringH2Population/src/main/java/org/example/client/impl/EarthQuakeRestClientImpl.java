package org.example.client.impl;

import jakarta.annotation.PostConstruct;
import org.example.client.EarthQuakeClient;
import org.example.dto.EarthQuakeCreateRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class EarthQuakeRestClientImpl implements EarthQuakeClient {

    private RestClient restClient;

    private static final String URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

    @PostConstruct
    private void init() {
        restClient = RestClient.builder()
                .baseUrl(URL)
                .build();
    }

//    @Override
//    public List<EarthQuakeCreateRequest> getEarthQuakes() {
//        return restClient.get()
//                .uri("/" + id)
//                .retrieve()
//                .body(CoursesResponse.class);
//    }

    @Override
    public EarthQuakeCreateRequest getEarthQuakes() {
        ParameterizedTypeReference<EarthQuakeCreateRequest> type = new ParameterizedTypeReference<>() {
        };

        return restClient.get()
                .retrieve()
                .body(EarthQuakeCreateRequest.class);
    }
}
