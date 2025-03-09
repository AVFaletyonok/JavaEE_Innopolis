package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.exception.RowWithIdDontExistsException;
import org.example.exception.WrongInputException;
import org.example.model.Account;
import org.example.repository.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class AccountRepositoryImpl implements Repository<Account> {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String COUNT = "select count(id) from accounts";
    private static final String CHECK_IS_EXIST_BY_ID =
            "select count(id) from accounts where id = ";
    private static final String FIND_BY_ID = "select * from accounts where id = ";
    private static final String FIND_ALL = "select * from accounts";
    private static final String INSERT = "insert into accounts (id, id_contract, amount) values (?, ?, ?)";
    private static final String UPDATE = "update accounts set id_contract = ?, amount = ? where id = ?";
    private static final String DELETE_BY_ID = "delete from accounts where id = ?";
    private static final String DELETE_ALL = "truncate table accounts";

    @Override
    public Long getCount() {
        try{
            return jdbcTemplate.queryForObject(COUNT, Long.class);
        } catch (DataAccessException e) {
            log.info("Error while request for the count of accounts table rows");
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
    public boolean checkIsInputRight(Account account) {
        return account.getId() != null && account.getIdContract() != null &&
                account.getId() > 0 && account.getIdContract() > 0 &&
                account.getAmount().longValue() >= 0;
    }

    @Override
    public void create(Account account) {
        try {
            jdbcTemplate.update(INSERT,
                    account.getId(), account.getIdContract(), account.getAmount());
        } catch (DataAccessException e) {
            log.info("Error while creating db row of new account: " + e.getMessage());
        }
    }

    public Account findById(final Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID + id, accountRowMapper);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public List<Account> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL, accountRowMapper);
        } catch (DataAccessException e) {
            log.info("Error while finding all accounts in db: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Account account) {
        try {
            if (!checkIsInputRight(account)) {
                throw new WrongInputException(account.toString());
            }
            if (checkIsExistById(account.getId())) {
                jdbcTemplate.update(UPDATE, account.getIdContract(), account.getAmount(), account.getId());
            } else {
                create(account);
            }
        } catch (DataAccessException e) {
            log.info("Error while updating the account in db: " + e.getMessage());
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
            log.info("Error while deleting all rows of the accounts in db: " + e.getMessage());
        }
    }

    private static final RowMapper<Account> accountRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idCountract = row.getLong("id_contract");
        BigDecimal amount = row.getBigDecimal("amount");

        return new Account(id, idCountract, amount);
    };
}
