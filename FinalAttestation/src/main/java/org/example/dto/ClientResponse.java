package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представление клиента.
 *
 * @author Faletyonok ALexander
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {

    /**
     * Имя клиента.
     */
    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    /**
     * Фамилия клиента.
     */
    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    /**
     * Номер телефона клиента.
     */
    @JsonProperty("phoneNumber")
    @NotNull
    private String phoneNumber;

    /**
     * Email клиента.
     */
    @NotNull
    private String email;

    /**
     * Аккаунт клиента.
     */
    @JsonProperty("accountResponse")
    private AccountResponse accountResponse;
}
