package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AccountResponse;
import org.example.dto.ClientResponse;
import org.example.dto.NotificationKafkaMessage;
import org.example.dto.TransactionRequest;
import org.example.entity.Account;
import org.example.entity.ClientEntity;
import org.example.entity.Transaction;
import org.example.entity.TransactionTypes;
import org.example.kafka.KafkaProducer;
import org.example.repository.AccountRepository;
import org.example.repository.ClientRepository;
import org.example.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с клиентами.
 *
 * @author Faletyonok Alexander
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    /**
     * Кафка производитель - для отправки асинхронных сообщений клиентам.
     */
    private final KafkaProducer kafkaProducer;
    /**
     * Репозиторий для хранения данных клиентов.
     */
    private final ClientRepository clientRepository;
    /**
     * Репозиторий для хранения аккаунтов.
     */
    private final AccountRepository accountRepository;
    /**
     * Репозиторий для хранения транзакций.
     */
    private final TransactionRepository transactionRepository;

    /**
     * Map для хранения настроики безопасности для клиентов, админов.
     */
    private final UserDetailsManager userDetailsManager;
    /**
     * Кодировщик паролей - BCryptPasswordEncoder (see SecurityConfig.class).
     */
    private final PasswordEncoder encoder;

    /**
     * Метод для конкертации сущности клиента из базы данных в представление клиента.
     *
     * @param client конкертируемая сущность клиента.
     * @return представление клиента.
     */
    private static ClientResponse convertToClientResponse(final ClientEntity client) {

        ClientResponse clientResponse =
                ClientResponse.builder()
                        .firstName(client.getFirstName())
                        .lastName(client.getLastName())
                        .phoneNumber(client.getPhoneNumber())
                        .email(client.getEmail())
                        .build();

        if (client.getAccount() != null) {
            AccountResponse accountResponse =
                    new AccountResponse(client.getAccount().getContractNumber(),
                                        client.getAccount().getAmount());
            clientResponse.setAccountResponse(accountResponse);
        }

        return clientResponse;
    }

    /**
     * Метод для сохранения транзакций в базу данных.
     *
     * @param transactionRequest совершаемая транзакция.
     * @param account аккаунт клиента, совершающего транзакцию.
     * @param receiverId идентификатор получателя транзакции.
     */
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

    /**
     * Метод для регистрации нового клиента в системе UserDetailsManager.
     *
     * @param client создаваемого клиента.
     */
    private void registerClientUserDetailsManager(ClientEntity client) {
        userDetailsManager.createUser(User.builder()
                                            .username(client.getEmail())
                                            .password(client.getPassword())
                                            .roles("USER")
                                            .build());
    }

    /**
     * Метод для обновления данных клиента в системе UserDetailsManager.
     *
     * @param client обновляемого клиента.
     */
    private void changePasswordUserDetailsManager(ClientEntity client) {
        if (!userDetailsManager.userExists(client.getEmail())) {
            registerClientUserDetailsManager(client);
            return;
        }
        userDetailsManager.updateUser(User.builder()
                                            .username(client.getEmail())
                                            .password(client.getPassword())
                                            .roles("USER")
                                            .build());
    }

    /**
     * Метод для получения конкретного клиента.
     *
     * @param clientId запрашиваемого клиента.
     * @return искомого клиента.
     */
    @Cacheable(value = "clients", key = "#clientId", unless = "#result == null")
    public Optional<ClientResponse> getClient(final Long clientId) {
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(clientId);
        if (clientDBOptional.isEmpty() || clientDBOptional.get().isDeletedFlag()) {
            return Optional.empty();
        }
        ClientEntity clientEntity = clientDBOptional.get();
        ClientResponse clientResponse = convertToClientResponse(clientEntity);

        return Optional.of(clientResponse);
    }

    /**
     * Метод для получения всех клиентов.
     *
     * @return Все клиенты.
     */
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

    /**
     * Метод для создания нового клиента.
     *
     * @param client создаваемый клиент.
     * @return созданного клиента.
     */
    @CachePut(value = "clients", key = "#client.id", unless = "#result == null")
    public Optional<ClientResponse> createClient(ClientEntity client) {

        if (client == null ||
                !client.getPassword().equals(client.getPasswordConfirm()) ||
                clientRepository.existsByPassportNumber(client.getPassportNumber()) ||
                clientRepository.existsByPhoneNumber(client.getPhoneNumber()) ||
                clientRepository.existsByEmail(client.getEmail())) {
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

    /**
     * Метод для обновления конкретного клиента.
     *
     * @param client обновляемый клиент.
     * @return обновленного клиента.
     */
    @CachePut(value = "clients", key = "#client.id", unless = "#result == null")
    public Optional<ClientResponse> updateClient(ClientEntity client) {
        if (client == null || client.getId() == null ||
                !client.getPassword().equals(client.getPasswordConfirm())) {
            return Optional.empty();
        }
        Optional<ClientEntity> clientByPassport = clientRepository.findByPassportNumber(client.getPassportNumber());
        Optional<ClientEntity> clientByPhone = clientRepository.findByPhoneNumber(client.getPhoneNumber());
        Optional<ClientEntity> clientByEmail = clientRepository.findByEmail(client.getEmail());
        if (clientByPassport.isPresent() && !clientByPassport.get().getId().equals(client.getId()) ||
                clientByPhone.isPresent() && !clientByPhone.get().getId().equals(client.getId()) ||
                clientByEmail.isPresent() && !clientByEmail.get().getId().equals(client.getId())) {
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
        clientDB.setEmail(client.getEmail());
        clientDB.setDeletedFlag(client.isDeletedFlag());
        if (client.getAccount() != null) {
            clientDB.setAccount(client.getAccount());
        }
        clientDB = clientRepository.saveAndFlush(clientDB);
        changePasswordUserDetailsManager(client);
        ClientResponse clientResponse = convertToClientResponse(clientDB);

        return Optional.of(clientResponse);
    }

    /**
     * Метод для удаления конкретного клиента.
     * Устанавливает поле deletedFlag в состояние true.
     *
     * @param clientId удаляемого клиента.
     * @return удаленного клиента.
     */
    @CacheEvict(value = "clients", key = "#clientId")
    public Optional<ClientResponse> deleteClient(Long clientId) {
        Optional<ClientEntity> clientDBOptional = clientRepository.findById(clientId);
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

    /**
     * Метод для создания аккаунта конкретному клиенту.
     *
     * @param clientId запрашиваемого клиента.
     * @return обновленного клиента.
     */
    @CachePut(value = "clients", key = "#clientId", unless = "#result == null")
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

    /**
     * Метод для совершения транзакций: переводов/пополнений аккаунтов.
     *
     * @param clientId клиента, осуществляющего транзакцию.
     * @param transaction совершаемая транзакция.
     * @return обновленного клиента, совершившего транзакцию.
     */
    @CachePut(value = "clients", key = "#clientId", unless = "#result == null")
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

        String text = MessageFormat.format("Dear, {0} {1}!\n" +
                      "A transaction : {2} with amount : {3} has been completed to your account.\n" +
                      "Total amount of your account : {4}.",
                      clientReceiver.getFirstName(), clientReceiver.getLastName(),
                      transaction.getType().toString(), transaction.getAmount(), clientReceiver.getAccount().getAmount());
        kafkaProducer.sendMessage(new NotificationKafkaMessage(clientReceiver.getEmail(), text), "topic-1");

        ClientResponse clientResponse = convertToClientResponse(clientSender);
        return Optional.of(clientResponse);
    }
}
