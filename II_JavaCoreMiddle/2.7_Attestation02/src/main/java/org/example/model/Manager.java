package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Manager {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
}
