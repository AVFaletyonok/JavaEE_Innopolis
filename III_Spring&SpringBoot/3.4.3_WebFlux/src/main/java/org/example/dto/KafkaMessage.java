package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class KafkaMessage {

    private Long id;
    private String message;
    private LocalDateTime sendTime;
}
