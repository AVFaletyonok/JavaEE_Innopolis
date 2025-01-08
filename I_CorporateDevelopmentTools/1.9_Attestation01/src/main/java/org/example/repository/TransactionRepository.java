package org.example.repository;

import org.example.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> findAll();

    void insert(Transaction transaction);

    void update(Transaction transaction, BigDecimal newAmount);

    void delete(Transaction transaction);
}
