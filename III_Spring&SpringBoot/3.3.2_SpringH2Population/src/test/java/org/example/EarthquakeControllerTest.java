package org.example;

import org.example.controller.EarthQuakeController;
import org.example.dto.EarthQuakeCreateRequest;
import org.example.dto.EarthQuakeResponse;
import org.example.entity.EarthQuakeEntity;
import org.example.service.EarthQuakeService;
import org.example.service.impl.EarthQuakeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EarthQuakeController.class)
@ContextConfiguration(classes = EarthQuakeController.class)
public class EarthquakeControllerTest {

    @Autowired
    private EarthQuakeController earthQuakeController;
    @MockitoBean
    private EarthQuakeServiceImpl earthQuakeService;
    @Autowired
    private MockMvc mvc;

    private EarthQuakeCreateRequest.Feature feature = new EarthQuakeCreateRequest.Feature(
            new EarthQuakeCreateRequest.Properties("M 0.8 - 2 km NNW of The Geysers, CA", 1744930549200L,
                    "2 km NNW of The Geysers, CA",0.79));
    private EarthQuakeCreateRequest earthQuakeCreateRequest =
            new EarthQuakeCreateRequest(List.of(feature));

    private final String earthQuakeRequestJson = "{\"features\": ["
                                                + "{\"properties\": {"
                                                + "\"title\":\"M 0.8 - 2 km NNW of The Geysers, CA\","
                                                + "\"time\": 1744930549200,"
                                                + "\"place\":\"2 km NNW of The Geysers, CA\","
                                                + "\"mag\": 0.79"
                                                + "} } ] }";

    private EarthQuakeResponse earthQuakeResponse =
            new EarthQuakeResponse(6L, "M 2.6 - 3 km SSW of Julian, CA",
                    LocalDateTime.ofEpochSecond(1744929102790L, 0, ZoneOffset.UTC),
                    "3 km SSW of Julian, CA",2.6);
    private List<EarthQuakeResponse> earthQuakeResponseList = List.of(earthQuakeResponse);

    @Test
    void addEarthQuakeWithoutAnyException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/earthquakes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(earthQuakeRequestJson))
            .andExpect(status().isOk());
        Mockito.verify(earthQuakeService).addEarthQuakes(Mockito.any());
    }

    @Test
    void getEarthQuakes() throws Exception {
        Mockito.when(earthQuakeService.getEarthQuakes())
                        .thenReturn(earthQuakeResponseList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/earthquakes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(earthQuakeResponse.getId()))
                .andExpect(jsonPath("$.[0].title").value(earthQuakeResponse.getTitle()))
                .andExpect(jsonPath("$.[0].place").value(earthQuakeResponse.getPlace()))
                .andExpect(jsonPath("$.[0].mag").value(earthQuakeResponse.getMag()));
        Mockito.verify(earthQuakeService).getEarthQuakes();
    }

    @Test
    void getEarthQuakesByMagAbove() throws Exception {
        Mockito.when(earthQuakeService.getEarthQuakesByMagAbove(2.5))
                .thenReturn(earthQuakeResponseList);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/earthquakes/magnitude_above=2.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(earthQuakeResponse.getId()))
                .andExpect(jsonPath("$.[0].title").value(earthQuakeResponse.getTitle()))
                .andExpect(jsonPath("$.[0].place").value(earthQuakeResponse.getPlace()))
                .andExpect(jsonPath("$.[0].mag").value(earthQuakeResponse.getMag()));
        Mockito.verify(earthQuakeService).getEarthQuakesByMagAbove(Mockito.any());
    }

    @Test
    void getEarthQuakesByDeltaTime() throws Exception {
        String start = "2025-03-21T06:01:31";
        String end = "2025-04-21T06:01:31";
        Mockito.when(earthQuakeService.getEarthQuakesByDeltaTime(start, end))
                .thenReturn(earthQuakeResponseList);
        mvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/earthquakes/delta_time=" + start + "_" + end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(earthQuakeResponse.getId()))
                .andExpect(jsonPath("$.[0].title").value(earthQuakeResponse.getTitle()))
                .andExpect(jsonPath("$.[0].place").value(earthQuakeResponse.getPlace()))
                .andExpect(jsonPath("$.[0].mag").value(earthQuakeResponse.getMag()));
        Mockito.verify(earthQuakeService).getEarthQuakesByDeltaTime(Mockito.any(), Mockito.any());
    }
}
