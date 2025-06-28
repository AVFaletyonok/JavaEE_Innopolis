package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AccountResponse;
import org.example.dto.ClientResponse;
import org.example.dto.TransactionRequest;
import org.example.entity.Account;
import org.example.entity.ClientEntity;
import org.example.entity.Transaction;
import org.example.entity.TransactionTypes;
import org.example.repository.AccountRepository;
import org.example.repository.ClientRepository;
import org.example.repository.TransactionRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder encoder;

    private static ClientResponse convertToClientResponse(final ClientEntity client) {

        ClientResponse clientResponse =
                ClientResponse.builder()
                        .firstName(client.getFirstName())
                        .lastName(client.getLastName())
                        .phoneNumber(client.getPhoneNumber())

                        .build();

        if (client.getAccount() != null) {
            AccountResponse accountResponse =
                    new AccountResponse(client.getAccount().getContractNumber(),
                                        client.getAccount().getAmount());
            clientResponse.setAccountResponse(accountResponse);
        }

        return clientResponse;
    }

    private void saveTransaction(TransactionRequest transactionRequest,
                                 Account account, Long receiverId) {
        Transaction transaction =
                Transaction.builder()
                        .account(account)
                        .receiverId(receiverId)
                        .amount(transactionRequest.getAmount())
                        .type(transactionRequest.getType())
                        .dateTime(LocalDateTime.now())
                        .build();
        transactionRepository.saveAndFlush(transaction);
    }

    private void registerClientUserDetailsManager(ClientEntity client) {
        userDetailsManager.createUser(User.builder()
                                            .username("client-" + client.getId())
                                            .password(client.getPassword())
                                            .roles("USER")
                                            .build());
    }

    private void changePasswordUserDetailsManager(ClientEntity client) {
        if (!userDetailsManager.userExists("client-" + client.getId())) {
            registerClientUserDetailsManager(client);
            return;
        }
        userDetailsManager.updateUser(User.builder()
                                            .username("client-" + client.getId())
                                            .password(client.getPassword())
                                            .roles("USER")
                                            .build());
    }

    public Optional<ClientResponse> getClient(final Long id) {
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(id);
        if (clientDBOptional.isEmpty() || clientDBOptional.get().isDeletedFlag()) {
            return Optional.empty();
        }
        ClientEntity clientEntity = clientDBOptional.get();
        ClientResponse clientResponse = convertToClientResponse(clientEntity);

        return Optional.of(clientResponse);
    }

    public Optional<List<ClientResponse>> getAllClients() {
        List<ClientEntity> clients =
                clientRepository.findAll()
                        .stream()
                        .filter(client -> !client.isDeletedFlag())
                        .toList();
        if (clients.isEmpty()) {
            return Optional.empty();
        }
        List<ClientResponse> clientResponses =
                clients.stream()
                        .map(ClientService::convertToClientResponse)
                        .toList();

        return Optional.of(clientResponses);
    }

    public Optional<ClientResponse> createClient(ClientEntity client) {

        if (client == null ||
                !client.getPassword().equals(client.getPasswordConfirm()) ||
                clientRepository.existsByPassportNumber(client.getPassportNumber()) ||
                clientRepository.existsByPhoneNumber(client.getPhoneNumber())) {
            return Optional.empty();
        }
        client.setDeletedFlag(false);
        client.setPassword(encoder.encode(client.getPassword()));
        client.setPasswordConfirm(client.getPassword());

        ClientEntity clientDB = clientRepository.saveAndFlush(client);
        registerClientUserDetailsManager(client);
        ClientResponse clientResponse = convertToClientResponse(clientDB);

        return Optional.of(clientResponse);
    }

    public Optional<ClientResponse> updateClient(ClientEntity client) {
        if (client == null || client.getId() == null ||
                !client.getPassword().equals(client.getPasswordConfirm())) {
            return Optional.empty();
        }
        Optional<ClientEntity> clientByPassport = clientRepository.findByPassportNumber(client.getPassportNumber());
        Optional<ClientEntity> clientByPhone = clientRepository.findByPhoneNumber(client.getPhoneNumber());
        if (clientByPassport.isPresent() && !clientByPassport.get().getId().equals(client.getId()) ||
                clientByPhone.isPresent() && !clientByPhone.get().getId().equals(client.getId())) {
            return Optional.empty();
        }
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(client.getId());
        if (clientDBOptional.isEmpty()) {
            return Optional.empty();
        }
        ClientEntity clientDB = clientDBOptional.get();
        clientDB.setFirstName(client.getFirstName());
        clientDB.setLastName(client.getLastName());
        clientDB.setPassword(encoder.encode(client.getPassword()));
        clientDB.setPasswordConfirm(client.getPasswordConfirm());
        clientDB.setPassportNumber(client.getPassportNumber());
        clientDB.setPhoneNumber(client.getPhoneNumber());
        clientDB.setDeletedFlag(client.isDeletedFlag());
        if (client.getAccount() != null) {
            clientDB.setAccount(client.getAccount());
        }
        clientDB = clientRepository.saveAndFlush(clientDB);
        changePasswordUserDetailsManager(client);
        ClientResponse clientResponse = convertToClientResponse(clientDB);

        return Optional.of(clientResponse);
    }

    public Optional<ClientResponse> deleteClient(Long id) {
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(id);
        if (clientDBOptional.isEmpty() ||
                clientDBOptional.get().isDeletedFlag()) {
            return Optional.empty();
        }
        ClientEntity clientEntity = clientDBOptional.get();
        clientEntity.setDeletedFlag(true);
        clientEntity.setPasswordConfirm(clientEntity.getPassword());
        clientEntity = clientRepository.saveAndFlush(clientEntity);
        ClientResponse clientResponse = convertToClientResponse(clientEntity);

        return Optional.of(clientResponse);
    }

    public Optional<ClientResponse> createAccount(Long clientId) {
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(clientId);
        if (clientDBOptional.isEmpty() || clientDBOptional.get().isDeletedFlag()) {
            return Optional.empty();
        }
        ClientEntity clientEntity = clientDBOptional.get();
        Account account =
                Account.builder()
                        .client(clientEntity)
                        .contractNumber(LocalDate.now().toString() + "-" + clientEntity.getId())
                        .amount(new BigDecimal(0))
                        .build();
        accountRepository.saveAndFlush(account);
        clientEntity.setAccount(account);
        clientEntity.setPasswordConfirm(clientEntity.getPassword());
        clientEntity = clientRepository.saveAndFlush(clientEntity);
        ClientResponse clientResponse = convertToClientResponse(clientEntity);

        return Optional.of(clientResponse);
    }

    public Optional<ClientResponse> makeTransaction(Long clientId, TransactionRequest transaction) {
        if (clientId == null || transaction == null ||
                transaction.getAmount().compareTo(new BigDecimal(0)) <= 0) {
            return Optional.empty();
        }
        Optional<ClientEntity> clientSenderOpt = clientRepository.findById(clientId);
        Optional<ClientEntity> clientReceiverOpt =
                clientRepository.findByPhoneNumber(transaction.getReceiverPhoneNumber());
        if (clientSenderOpt.isEmpty() || clientSenderOpt.get().isDeletedFlag() ||
                clientSenderOpt.get().getAccount() == null ||
                clientReceiverOpt.isEmpty() || clientReceiverOpt.get().isDeletedFlag() ||
                clientReceiverOpt.get().getAccount() == null ||
                (transaction.getType() == TransactionTypes.REPLENISHMENT &&
                    !clientSenderOpt.get().getId().equals(clientReceiverOpt.get().getId())) ||
                (transaction.getType() == TransactionTypes.TRANSFER &&
                    (clientSenderOpt.get().getId().equals(clientReceiverOpt.get().getId()) ||
                        clientSenderOpt.get().getAccount().getAmount().compareTo(transaction.getAmount()) < 0)) ) {
            return Optional.empty();
        }
        ClientEntity clientSender = clientSenderOpt.get();
        clientSender.setPasswordConfirm(clientSender.getPassword());
        ClientEntity clientReceiver = clientReceiverOpt.get();
        clientReceiver.setPasswordConfirm(clientReceiver.getPassword());

        switch (transaction.getType()) {
            case REPLENISHMENT : // Пополнение
                BigDecimal newAmount =
                        clientSender.getAccount().getAmount().add(transaction.getAmount());
                clientSender.getAccount().setAmount(newAmount);
                break;
            case TRANSFER: // Перевод
                BigDecimal newAmountSender =
                        clientSender.getAccount().getAmount().subtract(transaction.getAmount());
                clientSender.getAccount().setAmount(newAmountSender);
                BigDecimal newAmountReceiver =
                        clientReceiver.getAccount().getAmount().add(transaction.getAmount());
                clientReceiver.getAccount().setAmount(newAmountReceiver);
                clientRepository.saveAndFlush(clientReceiver);
        }
        saveTransaction(transaction, clientSender.getAccount(), clientReceiver.getId());
        clientSender = clientRepository.saveAndFlush(clientSender);
        ClientResponse clientResponse = convertToClientResponse(clientSender);

        return Optional.of(clientResponse);
    }
}
