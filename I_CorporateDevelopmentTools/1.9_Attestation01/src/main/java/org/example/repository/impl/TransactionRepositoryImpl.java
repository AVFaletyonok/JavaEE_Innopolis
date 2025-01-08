package org.example.repository.impl;

import org.example.config.JDBCTemplateConfig;
import org.example.entity.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String FIND_ALL = "select * from transactions";
    private static final String INSERT =
            "insert into transactions (id, id_account, amount, type)" +
                              "values (?, ?, ?, ?)";
    private static final String UPDATE = "update transactions set amount = ? where id = ?";
    private static final String DELETE = "delete from transactions where id = ?";

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query(FIND_ALL, transactionRowMapper);
    }

    @Override
    public void insert(Transaction transaction) {
        jdbcTemplate.update(INSERT,
                transaction.getId(), transaction.getIdAccount(),
                transaction.getAmount(), transaction.getType());
    }

    @Override
    public void update(Transaction transaction, BigDecimal newAmount) {
        jdbcTemplate.update(UPDATE, newAmount, transaction.getId());
    }

    @Override
    public void delete(Transaction transaction) {
        jdbcTemplate.update(DELETE, transaction.getId());
    }

    private static final RowMapper<Transaction> transactionRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idAccount = row.getLong("id_account");
        BigDecimal amount = row.getBigDecimal("amount");
        String type = row.getString("type");

        return new Transaction(id, idAccount, amount, type);
    };
}
