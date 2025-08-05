import org.example.dto.ClientResponse;
import org.example.dto.TransactionRequest;
import org.example.entity.Account;
import org.example.entity.ClientEntity;
import org.example.entity.TransactionTypes;
import org.example.repository.AccountRepository;
import org.example.repository.ClientRepository;
import org.example.repository.TransactionRepository;
import org.example.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;

@SpringBootTest
@ContextConfiguration(classes = ClientService.class)
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private UserDetailsManager userDetailsManager;
    @MockBean
    private PasswordEncoder encoder;

    private final ClientEntity correctClient =
            new ClientEntity(1L, "Anton", "Petrov",
                    "123", "123",
                    "1234:000001", "+1-999-999-99-01",
                    false, null);

    private final ClientEntity wrongClient =
            new ClientEntity(10L, null, null,
                    null, null,
                    null, null,
                    false, null);

    private final ClientEntity deletedClient =
            new ClientEntity(correctClient.getId(), correctClient.getFirstName(), correctClient.getLastName(),
                    correctClient.getPassword(), correctClient.getPasswordConfirm(),
                    correctClient.getPassportNumber(), correctClient.getPhoneNumber(),
                    true, correctClient.getAccount());

    private final Account account =
            new Account(1L, correctClient, "2026-06-28-1",
                        new BigDecimal(500), new ArrayList<>());
    private final ClientEntity correctClientWithAccount =
            new ClientEntity(correctClient.getId(), correctClient.getFirstName(), correctClient.getLastName(),
                    correctClient.getPassword(), correctClient.getPasswordConfirm(),
                    correctClient.getPassportNumber(), correctClient.getPhoneNumber(),
                    correctClient.isDeletedFlag(), account);

    private final TransactionRequest transactionRequest =
            new TransactionRequest("+1-999-999-99-01", new BigDecimal(100),
                                    TransactionTypes.REPLENISHMENT);

    @Test
    public void getExistingClient() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.of(correctClient));

        Optional<ClientResponse> clientResponseOpt = clientService.getClient(correctClient.getId());
        assertTrue(clientResponseOpt.isPresent());
        ClientResponse client = clientResponseOpt.get();
        assertEquals(correctClient.getFirstName(), client.getFirstName());
        assertEquals(correctClient.getLastName(), client.getLastName());
        assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
        assertNull(client.getAccountResponse());
    }

    @Test
    public void getNotExistingClient() {
        Mockito.when(clientRepository.findById(wrongClient.getId()))
                .thenReturn(Optional.empty());

        Optional<ClientResponse> clientResponseOpt = clientService.getClient(wrongClient.getId());
        assertTrue(clientResponseOpt.isEmpty());
    }

    @Test
    public void getClients() {
        Mockito.when(clientRepository.findAll())
                .thenReturn(List.of(correctClient));

        Optional<List<ClientResponse>> clientResponsesOpt = clientService.getAllClients();
        assertTrue(clientResponsesOpt.isPresent());
        List<ClientResponse> clientResponses = clientResponsesOpt.get();
        assertNotNull(clientResponses);
        assertEquals(1, clientResponses.size());
        ClientResponse client = clientResponses.get(0);
        assertEquals(correctClient.getFirstName(), client.getFirstName());
        assertEquals(correctClient.getLastName(), client.getLastName());
        assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
        assertNull(client.getAccountResponse());
    }

    @Test
    public void createCorrectClient() {
        Mockito.when(clientRepository.existsByPassportNumber(correctClient.getPassportNumber()))
                .thenReturn(false);
        Mockito.when(clientRepository.existsByPhoneNumber(correctClient.getPhoneNumber()))
                .thenReturn(false);
        Mockito.when(encoder.encode(correctClient.getPassword()))
                .thenReturn(correctClient.getPassword());
        Mockito.when(clientRepository.saveAndFlush(correctClient))
                .thenReturn(correctClient);
        doNothing().when(userDetailsManager).createUser(any(User.class));

        // Мокируем вызов приватного метода
        try (MockedConstruction<User> mockedUser = mockConstruction(User.class)) {

            Optional<ClientResponse> clientResponseOpt = clientService.createClient(correctClient);
            assertTrue(clientResponseOpt.isPresent());
            ClientResponse client = clientResponseOpt.get();
            assertEquals(correctClient.getFirstName(), client.getFirstName());
            assertEquals(correctClient.getLastName(), client.getLastName());
            assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
            assertNull(client.getAccountResponse());
        }
    }

    @Test
    public void createWrongClient() {
        Mockito.when(clientRepository.existsByPassportNumber(correctClient.getPassportNumber()))
                .thenReturn(true);

        Optional<ClientResponse> clientResponseOpt = clientService.createClient(correctClient);
        assertTrue(clientResponseOpt.isEmpty());
    }

    @Test
    public void updateCorrectClient() {
        Mockito.when(clientRepository.findByPassportNumber(correctClient.getPassportNumber()))
                .thenReturn(Optional.of(correctClient));
        Mockito.when(clientRepository.findByPhoneNumber(correctClient.getPhoneNumber()))
                .thenReturn(Optional.of(correctClient));
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.of(correctClient));
        Mockito.when(encoder.encode(correctClient.getPassword()))
                .thenReturn(correctClient.getPassword());
        Mockito.when(clientRepository.saveAndFlush(correctClient))
                .thenReturn(correctClient);
        Mockito.when(userDetailsManager.userExists("client-" + correctClient.getId()))
                .thenReturn(true);
        doNothing().when(userDetailsManager).updateUser(any(User.class));

        Optional<ClientResponse> clientResponseOpt = clientService.updateClient(correctClient);
        assertTrue(clientResponseOpt.isPresent());
        ClientResponse client = clientResponseOpt.get();
        assertEquals(correctClient.getFirstName(), client.getFirstName());
        assertEquals(correctClient.getLastName(), client.getLastName());
        assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
        assertNull(client.getAccountResponse());
    }

    @Test
    public void updateWrongClient() {
        Mockito.when(clientRepository.findByPassportNumber(correctClient.getPassportNumber()))
                .thenReturn(Optional.empty());

        Optional<ClientResponse> clientResponseOpt = clientService.updateClient(correctClient);
        assertTrue(clientResponseOpt.isEmpty());
    }

    @Test
    public void deleteExistingClient() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.of(correctClient));
        Mockito.when(clientRepository.saveAndFlush(
                Mockito.argThat(client ->
                    client.getFirstName().equals(correctClient.getFirstName()) &&
                    client.getLastName().equals(correctClient.getLastName()) &&
                    client.getPassword().equals(correctClient.getPassword()) &&
                    client.getPasswordConfirm().equals(correctClient.getPasswordConfirm()) &&
                    client.getPassportNumber().equals(correctClient.getPassportNumber()) &&
                    client.getPhoneNumber().equals(correctClient.getPhoneNumber())
                )))
                .thenReturn(deletedClient);

        Optional<ClientResponse> clientResponseOpt = clientService.deleteClient(correctClient.getId());
        assertTrue(clientResponseOpt.isPresent());
        ClientResponse client = clientResponseOpt.get();
        assertEquals(correctClient.getFirstName(), client.getFirstName());
        assertEquals(correctClient.getLastName(), client.getLastName());
        assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
        assertNull(client.getAccountResponse());
    }

    @Test
    public void deleteNotExistingClient() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.empty());

        Optional<ClientResponse> clientResponseOpt = clientService.deleteClient(correctClient.getId());
        assertTrue(clientResponseOpt.isEmpty());
    }

    @Test
    public void createAccountForExistingClient() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.of(correctClient));
        Account savedAccount = new Account(1L, correctClient, "2026-06-28-1",
                new BigDecimal(500), new ArrayList<>());
        Mockito.when(accountRepository.saveAndFlush(any(Account.class)))
                .thenReturn(savedAccount);

        ClientEntity savedClient = new ClientEntity(
                correctClient.getId(), correctClient.getFirstName(), correctClient.getLastName(),
                correctClient.getPassword(), correctClient.getPasswordConfirm(),
                correctClient.getPassportNumber(), correctClient.getPhoneNumber(),
                correctClient.isDeletedFlag(), savedAccount);

        Mockito.when(clientRepository.saveAndFlush(any(ClientEntity.class)))
                .thenReturn(savedClient);

        Optional<ClientResponse> clientResponseOpt = clientService.createAccount(correctClient.getId());
        assertTrue(clientResponseOpt.isPresent());
        ClientResponse client = clientResponseOpt.get();
        assertEquals(correctClient.getFirstName(), client.getFirstName());
        assertEquals(correctClient.getLastName(), client.getLastName());
        assertEquals(correctClient.getPhoneNumber(), client.getPhoneNumber());
        assertNotNull(client.getAccountResponse());
        assertEquals(savedAccount.getContractNumber(), client.getAccountResponse().getContractNumber());
        assertEquals(savedAccount.getAmount(), client.getAccountResponse().getAmount());
    }

    @Test
    public void createAccountForNotExistingClient() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.empty());

        Optional<ClientResponse> clientResponseOpt = clientService.createAccount(correctClient.getId());
        assertTrue(clientResponseOpt.isEmpty());
    }

    @Test
    public void makeCorrectTransaction() {
        BigDecimal oldAmount = account.getAmount();
        ClientEntity clientWithAccount = new ClientEntity(
                correctClient.getId(), correctClient.getFirstName(), correctClient.getLastName(),
                correctClient.getPassword(), correctClient.getPasswordConfirm(),
                correctClient.getPassportNumber(), correctClient.getPhoneNumber(),
                correctClient.isDeletedFlag(), account
        );

        Mockito.when(clientRepository.findById(clientWithAccount.getId()))
                .thenReturn(Optional.of(clientWithAccount));
        Mockito.when(clientRepository.findByPhoneNumber(transactionRequest.getReceiverPhoneNumber()))
                .thenReturn(Optional.of(clientWithAccount));
        Mockito.when(clientRepository.saveAndFlush(any(ClientEntity.class)))
                .thenReturn(clientWithAccount);

        Optional<ClientResponse> clientResponseOpt =
                clientService.makeTransaction(clientWithAccount.getId(), transactionRequest);
        assertTrue(clientResponseOpt.isPresent());
        ClientResponse client = clientResponseOpt.get();
        assertEquals(clientWithAccount.getFirstName(), client.getFirstName());
        assertEquals(clientWithAccount.getLastName(), client.getLastName());
        assertEquals(clientWithAccount.getPhoneNumber(), client.getPhoneNumber());
        assertNotNull(client.getAccountResponse());
        assertEquals(account.getContractNumber(), client.getAccountResponse().getContractNumber());

        BigDecimal expectedAmount = oldAmount.add(transactionRequest.getAmount());
        assertEquals(expectedAmount, client.getAccountResponse().getAmount());
    }

    @Test
    public void makeWrongTransaction() {
        Mockito.when(clientRepository.findById(correctClient.getId()))
                .thenReturn(Optional.empty());

        Optional<ClientResponse> clientResponseOpt =
                clientService.makeTransaction(correctClient.getId(), transactionRequest);
        assertTrue(clientResponseOpt.isEmpty());
    }
}
