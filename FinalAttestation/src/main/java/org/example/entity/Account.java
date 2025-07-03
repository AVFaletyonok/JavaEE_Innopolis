package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность аккаунта.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@Builder
public class Account {
    /**
     * id аккаунта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    /**
     * Связанный с аккаунтом клиент.
     */
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private ClientEntity client;

    /**
     * Номер контракта создания аккаунта.
     */
    @Column(name = "contract_number", nullable = false)
    @JsonProperty("contractNumber")
    private String contractNumber;

    /**
     * Сумма счета.
     */
    private BigDecimal amount;

    /**
     * Транзакции, выполненные с данного аккаунта.
     */
    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
