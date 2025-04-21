package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.EarthQuakeCreateRequest;
import org.example.dto.EarthQuakeResponse;
import org.example.service.impl.EarthQuakeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/earthquakes")
public class EarthQuakeController {

    private final EarthQuakeServiceImpl earthQuakeService;

    @PostMapping
    public ResponseEntity addEarthQuake(@RequestBody EarthQuakeCreateRequest request) {
        log.info("Start of adding a new earthquake");
        earthQuakeService.addEarthQuakes(request);
        log.info("The earthquakes are successful added");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<EarthQuakeResponse>> getEarthQuakes() {
        var response = earthQuakeService.getEarthQuakes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/magnitude_above={mag}")
    public ResponseEntity<List<EarthQuakeResponse>> getEarthQuakesByMagAbove(@PathVariable("mag") String mag) {
        log.info("Request to get earthquakes with mag above than: {}", mag);
        var response = earthQuakeService.getEarthQuakesByMagAbove(Double.valueOf(mag));
        log.info("{} earthquakes with mag above than {} were found", response.size(), mag);
        return ResponseEntity.ok(response);
    }

    // YYYY-MM-DDTHH:MM
    @GetMapping("/delta_time={start}_{end}")
    public ResponseEntity<List<EarthQuakeResponse>> getEarthQuakesByDeltaTime(@PathVariable("start") String start,
                                                                              @PathVariable("end") String end) {
        log.info("Request to get earthquakes by delta time from {} to {}", start, end);
        var response = earthQuakeService.getEarthQuakesByDeltaTime(start, end);
        log.info("{} earthquakes from {} to {} were found", response.size(), start, end);
        return ResponseEntity.ok(response);
    }
}