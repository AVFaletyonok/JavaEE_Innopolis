package org.example.repository;

import org.example.entity.Client;

import java.util.List;

public interface ClientRepository {

    List<Client> findAll();

    void insert(Client client);

    void updatePhoneNumber(Client client, String newPhoneNumber);

    void delete(Client client);
}
