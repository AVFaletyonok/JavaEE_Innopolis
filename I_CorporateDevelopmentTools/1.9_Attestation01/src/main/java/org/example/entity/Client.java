package org.example.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Client {
    private Long id;
    private String firstName;
    private String lastName;
    private String passportNumber;
    private String phoneNumber;
}
