package org.example.repository.impl;

import org.example.config.JDBCTemplateConfig;
import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

public class AccountRepositoryImpl implements AccountRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String FIND_ALL = "select * from accounts";
    private static final String INSERT = "insert into accounts (id, id_contract, amount) values (?, ?, ?)";
    private static final String UPDATE = "update accounts set amount = ? where id = ?";
    private static final String DELETE = "delete from accounts where id = ?";

    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(FIND_ALL, accountRowMapper);
    }

    @Override
    public void insert(Account account) {
        jdbcTemplate.update(INSERT,
                account.getId(), account.getIdContract(), account.getAmount());
    }

    @Override
    public void update(Account account, BigDecimal newAmount) {
        jdbcTemplate.update(UPDATE, newAmount, account.getId());
    }

    @Override
    public void delete(Account account) {
        jdbcTemplate.update(DELETE, account.getId());
    }

    private static final RowMapper<Account> accountRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idCountract = row.getLong("id_contract");
        BigDecimal amount = row.getBigDecimal("amount");

        return new Account(id, idCountract, amount);
    };
}
