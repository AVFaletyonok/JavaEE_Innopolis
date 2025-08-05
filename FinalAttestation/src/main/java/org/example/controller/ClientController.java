package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.dto.ClientResponse;
import org.example.dto.TransactionRequest;
import org.example.entity.ClientEntity;
import org.example.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Основной контроллер с endpoint-ами для взаимодействия с api clients.
 *
 * @author Faletyonok Alexander
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    /**
     * Репозиторий для хранения clients.
     */
    private final ClientService clientService;

    /**
     * Endpoint для получения конкретного клиента.
     *
     * @param id запрашиваемого клиента.
     * @return искомого клиента.
     */
    @Operation(summary = "Получение клиента по id.", description = "Получение клиента по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент с указанным id успешно найден."),
            @ApiResponse(responseCode = "404", description = "Клиент с указанным id не найден.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.getClient(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint для получения всех клиентов.
     *
     * @return Все клиенты.
     */
    @Operation(summary = "Получение всех клиентов.", description = "Получение всех клиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиенты успешно найдены."),
            @ApiResponse(responseCode = "404", description = "Список клиентов пуст.")
    })
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        Optional<List<ClientResponse>> clientResponsesOpt = clientService.getAllClients();
        return clientResponsesOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponsesOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint для создания нового клиента.
     *
     * @param client создаваемый клиент.
     * @return созданного клиента.
     */
    @Operation(summary = "Создание клиента.", description = "Создание клиента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент успешно создан."),
            @ApiResponse(responseCode = "400", description = "Клиент не создан.")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientEntity client) {
        Optional<ClientResponse> clientResponseOpt = clientService.createClient(client);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Endpoint для обновления конкретного клиента.
     *
     * @param client обновляемого клиента.
     * @return обновленного клиента.
     */
    @Operation(summary = "Обновление клиента по id", description = "Обновление клиента по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент с указанным id успешно обновлен."),
            @ApiResponse(responseCode = "404", description = "Клиент с указанным id не найден.")
    })
    @PutMapping
    public ResponseEntity<ClientResponse> updateClient(@RequestBody ClientEntity client) {
        Optional<ClientResponse> clientResponseOpt = clientService.updateClient(client);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Endpoint для удаления конкретного клиента.
     * Устанавливает поле deletedFlag в состояние true.
     *
     * @param id удаляемого клиента.
     * @return удаленного клиента.
     */
    @Operation(summary = "Удаление клиента по id.", description = "Удаление клиента по идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент с указанным id успешно удален."),
            @ApiResponse(responseCode = "404", description = "Клиент с указанным id не найден.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> deleteClient(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.deleteClient(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint для создания аккаунта конкретному клиенту.
     *
     * @param id запрашиваемого клиента.
     * @return обновленного клиента.
     */
    @PostMapping("/{id}/create-account")
    public ResponseEntity<ClientResponse> createAccount(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.createAccount(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint для совершения транзакций: переводов/пополнений аккаунтов.
     *
     * @param id клиента, осуществляющего транзакцию.
     * @param transaction совершаемая транзакция.
     * @return обновленного клиента, совершившего транзакцию.
     */
    @PostMapping("/{id}/make-transaction")
    public ResponseEntity<ClientResponse> makeTransaction(@PathVariable Long id,
                                                          @RequestBody TransactionRequest transaction) {
        Optional<ClientResponse> clientResponseOpt = clientService.makeTransaction(id, transaction);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
