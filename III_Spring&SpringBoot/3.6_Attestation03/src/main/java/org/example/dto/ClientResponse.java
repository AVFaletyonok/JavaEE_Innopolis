package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Account;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {

    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    @JsonProperty("phoneNumber")
    @NotNull
    private String phoneNumber;

    @JsonProperty("accountResponse")
    private AccountResponse accountResponse;
}
