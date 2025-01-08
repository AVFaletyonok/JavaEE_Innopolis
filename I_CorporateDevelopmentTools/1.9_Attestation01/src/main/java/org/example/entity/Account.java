package org.example.entity;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Account {
    private Long id;
    private Long idContract;
    private BigDecimal amount;
}
