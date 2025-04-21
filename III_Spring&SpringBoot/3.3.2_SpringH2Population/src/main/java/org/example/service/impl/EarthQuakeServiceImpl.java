package org.example.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.client.EarthQuakeClient;
import org.example.dto.EarthQuakeCreateRequest;
import org.example.dto.EarthQuakeResponse;
import org.example.entity.EarthQuakeEntity;
import org.example.repository.EarthQuakeRepository;
import org.example.service.EarthQuakeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EarthQuakeServiceImpl implements EarthQuakeService {

    private final EarthQuakeRepository earthQuakeRepository;
    private final EarthQuakeClient earthQuakeClient;

    @PostConstruct
    protected void init() {
        // init data
        EarthQuakeCreateRequest earthQuakes = earthQuakeClient.getEarthQuakes();
        addEarthQuakes(earthQuakes);
    }

    public void addEarthQuakes(EarthQuakeCreateRequest request) {
        List<EarthQuakeEntity> entities = request.getFeatures().stream()
                .map(e -> EarthQuakeEntity.builder()
                        .title(e.getProperties().getTitle())
                        .mag(e.getProperties().getMag())
                        .place(e.getProperties().getPlace())
                        .time(LocalDateTime.ofEpochSecond(e.getProperties().getTime()/1000, 0, ZoneOffset.UTC))
                        .build())
                .toList();
        earthQuakeRepository.saveAll(entities);
    }

    public List<EarthQuakeResponse> getEarthQuakes() {
        return earthQuakeRepository.findAll()
                .stream()
                .map(e -> EarthQuakeResponse.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .place(e.getPlace())
                        .time(e.getTime())
                        .mag(e.getMag())
                        .build())
                .toList();
    }

    public List<EarthQuakeResponse> getEarthQuakesByMagAbove(Double mag) {
        return earthQuakeRepository.findByMagAfter(mag)
                .stream()
                .map(e -> EarthQuakeResponse.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .place(e.getPlace())
                        .time(e.getTime())
                        .mag(e.getMag())
                        .build())
                .toList();
    }

    public List<EarthQuakeResponse> getEarthQuakesByDeltaTime(String start, String end) {
        // YYYY-MM-DDTHH:MM
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        return earthQuakeRepository.findByTimeBetween(startTime, endTime)
                .stream()
                .map(e -> EarthQuakeResponse.builder()
                        .id(e.getId())
                        .title(e.getTitle())
                        .place(e.getPlace())
                        .time(e.getTime())
                        .mag(e.getMag())
                        .build())
                .toList();
    }
}
