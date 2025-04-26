package org.example;

import jakarta.annotation.PostConstruct;
import org.example.client.EarthQuakeClient;
import org.example.dto.EarthQuakeCreateRequest;
import org.example.dto.EarthQuakeResponse;
import org.example.entity.EarthQuakeEntity;
import org.example.repository.EarthQuakeRepository;
import org.example.service.EarthQuakeService;
import org.example.service.impl.EarthQuakeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = TestEarthQuakeServiceImpl.class)
public class EarthQuakeServiceTest {

    @Autowired
    private EarthQuakeServiceImpl earthQuakeService;
    @MockitoBean
    private EarthQuakeRepository earthQuakeRepository;
    @MockitoBean
    private EarthQuakeClient earthQuakeClient;

    private EarthQuakeCreateRequest.Feature feature1 = new EarthQuakeCreateRequest.Feature(
            new EarthQuakeCreateRequest.Properties("M 0.8 - 2 km NNW of The Geysers, CA", 1744930549200L,
                    "2 km NNW of The Geysers, CA",0.79));
    private EarthQuakeCreateRequest.Feature feature2 = new EarthQuakeCreateRequest.Feature(
            new EarthQuakeCreateRequest.Properties("M 2.6 - 3 km SSW of Julian, CA", 1744929102790L,
                    "3 km SSW of Julian, CA",2.6));
    private EarthQuakeCreateRequest earthQuakeCreateRequest =
            new EarthQuakeCreateRequest(List.of(feature1, feature2));

    private EarthQuakeEntity earthQuakeEntity1 =
            new EarthQuakeEntity(6L, "M 2.6 - 3 km SSW of Julian, CA",
                                    LocalDateTime.ofEpochSecond(1744929102790L, 0, ZoneOffset.UTC),
                                    "3 km SSW of Julian, CA",2.6);
    private EarthQuakeEntity earthQuakeEntity2 =
            new EarthQuakeEntity(8L, "M 3.0 - 70 km SE of Chalkyitsik, Alaska",
                                    LocalDateTime.ofEpochSecond(1744927042728L, 0, ZoneOffset.UTC),
                                    "70 km SE of Chalkyitsik, Alaska",3.0);
    private List<EarthQuakeEntity> twoEarthQuakeEntityList =
            List.of(earthQuakeEntity1, earthQuakeEntity2);

    @Test
    public void addEarthQuakeWithoutAnyException() {
        earthQuakeService.addEarthQuakes(earthQuakeCreateRequest);
        Mockito.verify(earthQuakeRepository).saveAll(Mockito.any());
    }

    @Test
    public void getEarthQuakes() {
        Mockito.when(earthQuakeRepository.findAll())
                .thenReturn(twoEarthQuakeEntityList);
        var actualEarthQuakeList = earthQuakeService.getEarthQuakes();
        Mockito.verify(earthQuakeRepository).findAll();
        assertEquals(twoEarthQuakeEntityList.size(), actualEarthQuakeList.size());
        assertEquals(twoEarthQuakeEntityList.getFirst().getId(),
                actualEarthQuakeList.getFirst().getId());
        assertEquals(twoEarthQuakeEntityList.getFirst().getMag(),
                actualEarthQuakeList.getFirst().getMag());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTitle(),
                actualEarthQuakeList.getFirst().getTitle());
        assertEquals(twoEarthQuakeEntityList.getFirst().getPlace(),
                actualEarthQuakeList.getFirst().getPlace());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTime(),
                actualEarthQuakeList.getFirst().getTime());
    }

    @Test
    public void getEarthQuakesByMagAbove() {
        Mockito.when(earthQuakeRepository.findByMagAfter(2.5))
                .thenReturn(twoEarthQuakeEntityList);
        var actualEarthQuakeList = earthQuakeService.getEarthQuakesByMagAbove(2.5);
        Mockito.verify(earthQuakeRepository).findByMagAfter(2.5);
        assertEquals(twoEarthQuakeEntityList.size(), actualEarthQuakeList.size());
        assertEquals(twoEarthQuakeEntityList.getFirst().getId(),
                        actualEarthQuakeList.getFirst().getId());
        assertEquals(twoEarthQuakeEntityList.getFirst().getMag(),
                actualEarthQuakeList.getFirst().getMag());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTitle(),
                actualEarthQuakeList.getFirst().getTitle());
            assertEquals(twoEarthQuakeEntityList.getFirst().getPlace(),
                actualEarthQuakeList.getFirst().getPlace());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTime(),
                actualEarthQuakeList.getFirst().getTime());
    }

    @Test
    public void getEarthQuakesByDeltaTime() {
        String start = "2025-03-21T06:01:31";
        String end = "2025-04-21T06:01:31";
        Mockito.when(earthQuakeRepository.findByTimeBetween(LocalDateTime.parse(start),
                                                            LocalDateTime.parse(end)))
                .thenReturn(twoEarthQuakeEntityList);
        var actualEarthQuakeList = earthQuakeService.getEarthQuakesByDeltaTime(start, end);
        Mockito.verify(earthQuakeRepository).findByTimeBetween(LocalDateTime.parse(start),
                                                                LocalDateTime.parse(end));
        assertEquals(twoEarthQuakeEntityList.size(), actualEarthQuakeList.size());
        assertEquals(twoEarthQuakeEntityList.getFirst().getId(),
                actualEarthQuakeList.getFirst().getId());
        assertEquals(twoEarthQuakeEntityList.getFirst().getMag(),
                actualEarthQuakeList.getFirst().getMag());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTitle(),
                actualEarthQuakeList.getFirst().getTitle());
        assertEquals(twoEarthQuakeEntityList.getFirst().getPlace(),
                actualEarthQuakeList.getFirst().getPlace());
        assertEquals(twoEarthQuakeEntityList.getFirst().getTime(),
                actualEarthQuakeList.getFirst().getTime());
    }
}

class TestEarthQuakeServiceImpl extends EarthQuakeServiceImpl {

    public TestEarthQuakeServiceImpl(EarthQuakeRepository earthQuakeRepository,
                                EarthQuakeClient earthQuakeClient) {
        super(earthQuakeRepository, earthQuakeClient);
    }

    @Override
    @PostConstruct
    public void init() {
        // Пустая реализация
    }
}
