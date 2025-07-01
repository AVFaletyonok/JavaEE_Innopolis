package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Представление аккаунта.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    /**
     * Номер контракта создания аккаунта.
     */
    @JsonProperty("contractNumber")
    private String contractNumber;

    /**
     * Сумма счета.
     */
    private BigDecimal amount;
}
