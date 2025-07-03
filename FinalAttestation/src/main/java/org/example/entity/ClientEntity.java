package org.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность клиента.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class ClientEntity {
    /**
     * id клиента.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    /**
     * Имя клиента.
     */
    @Column(name = "first_name", columnDefinition = "LONGTEXT", nullable = false)
    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    /**
     * Фамилия клиента.
     */
    @Column(name = "last_name", columnDefinition = "LONGTEXT", nullable = false)
    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    /**
     * Пароль клиента.
     */
    @Column(nullable = false)
    @NotNull
    private String password;

    /**
     * Подтверждение пароля клиента.
     */
    @Transient
    @JsonProperty("passwordConfirm")
    @NotNull
    private String passwordConfirm;

    /**
     * Номер паспорта клиента.
     */
    @Column(name = "passport_number", columnDefinition = "LONGTEXT",
            nullable = false, unique = true)
    @JsonProperty("passportNumber")
    @NotNull
    private String passportNumber;

    /**
     * Номер телефона клиента.
     */
    @Column(name = "phone_number", columnDefinition = "LONGTEXT",
            nullable = false, unique = true)
    @JsonProperty("phoneNumber")
    @NotNull
    private String phoneNumber;

    /**
     * Email клиента.
     */
    @Column(columnDefinition = "LONGTEXT", nullable = false, unique = true)
    @Email
    @NotNull
    private String email;

    /**
     * Флаг мягкого удаления клиента.
     */
    @Column(name = "deleted_flag")
    private boolean deletedFlag;

    /**
     * Аккаунт клиента.
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
}
