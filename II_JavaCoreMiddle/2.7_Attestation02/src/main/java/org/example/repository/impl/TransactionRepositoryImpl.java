package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.exception.RowWithIdDontExistsException;
import org.example.exception.WrongInputException;
import org.example.model.Transaction;
import org.example.repository.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class TransactionRepositoryImpl implements Repository<Transaction> {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String COUNT = "select count(id) from transactions";
    private static final String CHECK_IS_EXIST_BY_ID =
            "select count(id) from transactions where id = ";
    private static final String INSERT =
            "insert into transactions (id, id_account, amount, type)" +
                    "values (?, ?, ?, ?)";
    private static final String FIND_BY_ID = "select * from transactions where id = ";
    private static final String FIND_ALL = "select * from transactions";
    private static final String UPDATE =
            "update transactions set id_account = ?, amount = ?, type = ? where id = ?";
    private static final String DELETE_BY_ID = "delete from transactions where id = ?";
    private static final String DELETE_ALL = "truncate table transactions";

    @Override
    public Long getCount() {
        try{
            return jdbcTemplate.queryForObject(COUNT, Long.class);
        } catch (DataAccessException e) {
            log.info("Error while request for the count of transactions table rows");
            return null;
        }
    }

    @Override
    public boolean checkIsExistById(final Long id) throws DataAccessException {
        try {
            int count = jdbcTemplate.queryForObject(CHECK_IS_EXIST_BY_ID + id, Integer.class);
            return count > 0;
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public boolean checkIsInputRight(Transaction transaction) {
        return transaction.getId() != null && transaction.getId() > 0 &&
                transaction.getIdAccount() != null && transaction.getIdAccount() > 0 &&
                transaction.getAmount().longValue() > 0 && transaction.getType() != null;
    }

    @Override
    public void create(Transaction transaction) {
        try {
            jdbcTemplate.update(INSERT,
                    transaction.getId(), transaction.getIdAccount(),
                    transaction.getAmount(), transaction.getType());
        } catch (DataAccessException e) {
            log.info("Error while creating db row of new transaction: " + e.getMessage());
        }
    }

    @Override
    public Transaction findById(final Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID + id, transactionRowMapper);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL, transactionRowMapper);
        } catch (DataAccessException e) {
            log.info("Error while finding all transactions in db: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Transaction transaction) {
        try {
            if (!checkIsInputRight(transaction)) {
                throw new WrongInputException(transaction.toString());
            }
            if (checkIsExistById(transaction.getId())) {
                jdbcTemplate.update(UPDATE, transaction.getIdAccount(), transaction.getAmount(),
                        transaction.getType(), transaction.getId());
            } else {
                create(transaction);
            }
        } catch (DataAccessException e) {
            log.info("Error while updating the transaction in db: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(final Long id) throws DataAccessException {
        try {
            jdbcTemplate.update(DELETE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            jdbcTemplate.update(DELETE_ALL);
        } catch (DataAccessException e) {
            log.info("Error while deleting all rows of the transactions in db: " + e.getMessage());
        }
    }

    private static final RowMapper<Transaction> transactionRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idAccount = row.getLong("id_account");
        BigDecimal amount = row.getBigDecimal("amount");
        String type = row.getString("type");

        return new Transaction(id, idAccount, amount, type);
    };
}
