package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarthQuakeCreateRequest {

    private List<Feature> features;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feature {

        private Properties properties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Properties {

        private String title;
        private Long time;
        private String place;
        private Double mag;
    }
}
