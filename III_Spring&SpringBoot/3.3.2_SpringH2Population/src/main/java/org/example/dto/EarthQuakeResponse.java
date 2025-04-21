package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarthQuakeResponse {

    private Long id;

    private String title;

    private LocalDateTime time;

    private String place;

    private Double mag;
}
