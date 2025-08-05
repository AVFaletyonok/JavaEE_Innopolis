import org.example.controller.ClientController;
import org.example.dto.AccountResponse;
import org.example.dto.ClientResponse;
import org.example.dto.TransactionRequest;
import org.example.entity.ClientEntity;
import org.example.entity.TransactionTypes;
import org.example.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = ClientController.class)
public class ClientControllerTest {

    @Autowired
    private ClientController clientController;
    @MockBean
    private ClientService clientService;
    @Autowired
    private MockMvc mvc;

    private final ClientEntity correctClient =
            new ClientEntity(1L, "Anton", "Petrov",
                            "123", "123",
                            "1234:000001", "+1-999-999-99-01",
                            false, null);
    private final ClientResponse correctClientResponse =
            new ClientResponse(correctClient.getFirstName(), correctClient.getLastName(),
                                correctClient.getPhoneNumber(), null);

    private final String correctClientJson = "{"
                                            + "\"firstName\":\"" + correctClient.getFirstName() + "\","
                                            + "\"lastName\":\"" + correctClient.getLastName() + "\","
                                            + "\"password\":\"" + correctClient.getPassword() + "\","
                                            + "\"passwordConfirm\":\"" + correctClient.getPasswordConfirm() + "\","
                                            + "\"passportNumber\":\"" + correctClient.getPassportNumber() + "\","
                                            + "\"phoneNumber\":\"" + correctClient.getPhoneNumber() + "\""
                                            + "}";

    private final ClientEntity wrongClient =
            new ClientEntity(10L, null, null,
                            null, null,
                            null, null,
                            false, null);
    private final String wrongClientJson = "{"
                                        + "\"firstName\":\"" + wrongClient.getFirstName() + "\","
                                        + "\"lastName\":\"" + wrongClient.getLastName() + "\","
                                        + "\"password\":\"" + wrongClient.getPassword() + "\","
                                        + "\"passwordConfirm\":\"" + wrongClient.getPasswordConfirm() + "\","
                                        + "\"passportNumber\":\"" + wrongClient.getPassportNumber() + "\","
                                        + "\"phoneNumber\":\"" + wrongClient.getPhoneNumber() + "\""
                                        + "}";

    private final AccountResponse accountResponse =
            new AccountResponse("2025-06-28-1", new BigDecimal(0));
    private final ClientResponse correctClientResponseWIthAccount =
            new ClientResponse(correctClient.getFirstName(), correctClient.getLastName(),
                    correctClient.getPhoneNumber(), accountResponse);

    @Test
    void getCorrectClient() throws Exception {
        Mockito.when(clientService.getClient(correctClient.getId()))
                .thenReturn(Optional.of(correctClientResponse));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients/" + correctClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").doesNotExist());
    }

    @Test
    void getWrongClient() throws Exception {
        Mockito.when(clientService.getClient(10L))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllClients() throws Exception {
        Mockito.when(clientService.getAllClients())
                .thenReturn(Optional.of(List.of(correctClientResponse)));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.[0].phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.[0].accountResponse").doesNotExist());
    }

    @Test
    void createCorrectClient() throws Exception {
        Mockito.when(clientService.createClient(
                Mockito.argThat(client ->
                        client.getFirstName().equals(correctClient.getFirstName()) &&
                        client.getLastName().equals(correctClient.getLastName()) &&
                        client.getPassword().equals(correctClient.getPassword()) &&
                        client.getPasswordConfirm().equals(correctClient.getPasswordConfirm()) &&
                        client.getPassportNumber().equals(correctClient.getPassportNumber()) &&
                        client.getPhoneNumber().equals(correctClient.getPhoneNumber())
                )))
                .thenReturn(Optional.of(correctClientResponse));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctClientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").doesNotExist());
    }

    @Test
    void createWrongClient() throws Exception {
        Mockito.when(clientService.createClient(wrongClient))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongClientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCorrectClient() throws Exception {
        Mockito.when(clientService.updateClient(
                Mockito.argThat(client ->
                        client.getFirstName().equals(correctClient.getFirstName()) &&
                                client.getLastName().equals(correctClient.getLastName()) &&
                                client.getPassword().equals(correctClient.getPassword()) &&
                                client.getPasswordConfirm().equals(correctClient.getPasswordConfirm()) &&
                                client.getPassportNumber().equals(correctClient.getPassportNumber()) &&
                                client.getPhoneNumber().equals(correctClient.getPhoneNumber())
                )))
                .thenReturn(Optional.of(correctClientResponse));
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").doesNotExist());
    }

    @Test
    void updateWrongClient() throws Exception {
        Mockito.when(clientService.updateClient(
                Mockito.argThat(client ->
                        client.getFirstName().equals(correctClient.getFirstName()) &&
                                client.getLastName().equals(correctClient.getLastName()) &&
                                client.getPassword().equals(correctClient.getPassword()) &&
                                client.getPasswordConfirm().equals(correctClient.getPasswordConfirm()) &&
                                client.getPassportNumber().equals(correctClient.getPassportNumber()) &&
                                client.getPhoneNumber().equals(correctClient.getPhoneNumber())
                )))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctClientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCorrectClient() throws Exception {
        Mockito.when(clientService.deleteClient(correctClient.getId()))
                .thenReturn(Optional.of(correctClientResponse));
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/clients/" + correctClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").doesNotExist());
    }

    @Test
    void deleteWrongClient() throws Exception {
        Mockito.when(clientService.deleteClient(wrongClient.getId()))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + wrongClient.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAccountToCorrectClient() throws Exception {
        Mockito.when(clientService.createAccount(correctClient.getId()))
                        .thenReturn(Optional.of(correctClientResponseWIthAccount));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/clients/" + correctClient.getId() + "/create-account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").exists())
                .andExpect(jsonPath("$.accountResponse.contractNumber").value(accountResponse.getContractNumber()))
                .andExpect(jsonPath("$.accountResponse.amount").value(accountResponse.getAmount()));
    }

    @Test
    void createAccountToWrongClient() throws Exception {
        Mockito.when(clientService.createAccount(wrongClient.getId()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/clients/" + wrongClient.getId() + "/create-account"))
                .andExpect(status().isNotFound());
    }

    @Test
    void makeCorrectTransaction() throws Exception {
        TransactionRequest transactionRequest =
                new TransactionRequest(correctClient.getPhoneNumber(), new BigDecimal(100),
                                        TransactionTypes.REPLENISHMENT);

        String transactionRequestJson = "{"
                                        + "\"receiverPhoneNumber\":\"" + transactionRequest.getReceiverPhoneNumber() + "\","
                                        + "\"amount\":\"" + transactionRequest.getAmount() + "\","
                                        + "\"type\":\"" + transactionRequest.getType() + "\""
                                        + "}";

        Mockito.when(clientService.makeTransaction(correctClient.getId(), transactionRequest))
                .thenReturn(Optional.of(correctClientResponseWIthAccount));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/clients/" + correctClient.getId() + "/make-transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(correctClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctClient.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(correctClient.getPhoneNumber()))
                .andExpect(jsonPath("$.accountResponse").exists())
                .andExpect(jsonPath("$.accountResponse.contractNumber").value(accountResponse.getContractNumber()))
                .andExpect(jsonPath("$.accountResponse.amount").value(accountResponse.getAmount()));
    }

    @Test
    void makeWrongTransaction() throws Exception {
        TransactionRequest transactionRequest =
                new TransactionRequest(null, new BigDecimal(100),
                        TransactionTypes.REPLENISHMENT);
        String transactionRequestJson = "{"
                + "\"receiverPhoneNumber\":\"" + transactionRequest.getReceiverPhoneNumber() + "\","
                + "\"amount\":\"" + transactionRequest.getAmount() + "\","
                + "\"type\":\"" + transactionRequest.getType() + "\""
                + "}";

        Mockito.when(clientService.makeTransaction(correctClient.getId(), transactionRequest))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/clients/" + correctClient.getId() + "/make-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionRequestJson))
                .andExpect(status().isBadRequest());
    }
}
