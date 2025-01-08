package org.example.repository;

import org.example.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    void insert(Account account);

    void update(Account account, BigDecimal newAmount);

    void delete(Account account);
}
