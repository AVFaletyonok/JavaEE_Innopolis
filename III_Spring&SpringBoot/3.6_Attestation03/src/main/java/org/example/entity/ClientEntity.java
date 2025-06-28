package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(name = "first_name", columnDefinition = "LONGTEXT", nullable = false)
    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    @Column(name = "last_name", columnDefinition = "LONGTEXT", nullable = false)
    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    @Column(nullable = false)
    @NotNull
    private String password;

    @Transient
    @JsonProperty("passwordConfirm")
    @NotNull
    private String passwordConfirm;

    @Column(name = "passport_number", columnDefinition = "LONGTEXT",
            nullable = false, unique = true)
    @JsonProperty("passportNumber")
    @NotNull
    private String passportNumber;

    @Column(name = "phone_number", columnDefinition = "LONGTEXT",
            nullable = false, unique = true)
    @JsonProperty("phoneNumber")
    @NotNull
    private String phoneNumber;

    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
