package org.example.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Contract {
    private Long id;
    private Long idClient;
    private Long idManager;
}
