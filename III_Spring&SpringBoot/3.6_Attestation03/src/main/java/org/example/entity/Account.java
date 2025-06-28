package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private ClientEntity client;

    @Column(name = "contract_number", nullable = false)
    @JsonProperty("contractNumber")
    private String contractNumber;

    private BigDecimal amount;

    @Builder.Default
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
