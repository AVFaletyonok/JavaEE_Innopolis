package org.example.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Transaction {
    private Long id;
    private Long idAccount;
    private BigDecimal amount;
    private String type;
}
