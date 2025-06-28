package org.example.controller;

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

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.getClient(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        Optional<List<ClientResponse>> clientResponsesOpt = clientService.getAllClients();
        return clientResponsesOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponsesOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientEntity client) {
        Optional<ClientResponse> clientResponseOpt = clientService.createClient(client);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<ClientResponse> updateClient(@RequestBody ClientEntity client) {
        Optional<ClientResponse> clientResponseOpt = clientService.updateClient(client);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> deleteClient(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.deleteClient(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{id}/create-account")
    public ResponseEntity<ClientResponse> createAccount(@PathVariable Long id) {
        Optional<ClientResponse> clientResponseOpt = clientService.createAccount(id);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/{id}/make-transaction")
    public ResponseEntity<ClientResponse> makeTransaction(@PathVariable Long id,
                                                          @RequestBody TransactionRequest transaction) {
        Optional<ClientResponse> clientResponseOpt = clientService.makeTransaction(id, transaction);
        return clientResponseOpt.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(clientResponseOpt.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
