package org.example.service;

import org.example.dto.EarthQuakeCreateRequest;
import org.example.dto.EarthQuakeResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EarthQuakeService {

    void addEarthQuakes(EarthQuakeCreateRequest request);

    List<EarthQuakeResponse> getEarthQuakes();

    List<EarthQuakeResponse> getEarthQuakesByMagAbove(Double mag);

    List<EarthQuakeResponse> getEarthQuakesByDeltaTime(String start, String end);
}
