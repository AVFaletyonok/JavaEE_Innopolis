package org.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderDTO {
    private Long id;
    private Long productNumber;
    private Integer count;
    private BigDecimal amount;
    private Timestamp timestamp;

    public void createOrder(OrderDTO orderDTO) {

    }
}
