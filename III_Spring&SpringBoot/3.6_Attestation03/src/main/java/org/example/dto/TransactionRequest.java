package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TransactionTypes;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @JsonProperty("receiverPhoneNumber")
    @NotNull
    private String receiverPhoneNumber;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionTypes type;
}
