package org.example.repository;

import org.example.entity.Contract;

import java.util.List;

public interface ContractRepository {

    List<Contract> findAll();

    void insert(Contract contract);

    void update(Contract contract, Long newIdManager);

    void delete(Contract contract);
}
