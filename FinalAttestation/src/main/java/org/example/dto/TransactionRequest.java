package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TransactionTypes;

import java.math.BigDecimal;

/**
 * Представление транзакции.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    /**
     * Номер телефона получателя транзакции.
     */
    @JsonProperty("receiverPhoneNumber")
    @NotNull
    private String receiverPhoneNumber;

    /**
     * Сумма транзакции.
     */
    @NotNull
    private BigDecimal amount;

    /**
     * Тип транзакции.
     */
    @NotNull
    private TransactionTypes type;
}
