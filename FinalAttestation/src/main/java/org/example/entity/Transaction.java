package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность транзакции.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
@Builder
public class Transaction {
    /**
     * id транзакции.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    /**
     * Аккаунт, с которого была выполнена транзакция.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    /**
     * id получателя транзакции.
     */
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    /**
     * Сумма транзакции.
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * Тип транзакции.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransactionTypes type;

    /**
     * Дата, время совершения транзакции.
     */
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
}
