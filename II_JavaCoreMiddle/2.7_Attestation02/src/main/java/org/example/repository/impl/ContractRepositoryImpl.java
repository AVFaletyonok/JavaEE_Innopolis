package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.exception.RowWithIdDontExistsException;
import org.example.exception.WrongInputException;
import org.example.model.Contract;
import org.example.repository.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Slf4j
public class ContractRepositoryImpl implements Repository<Contract> {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String COUNT = "select count(id) from contracts";
    private static final String CHECK_IS_EXIST_BY_ID =
            "select count(id) from contracts where id = ";
    private static final String FIND_BY_ID = "select * from contracts where id = ";
    private static final String FIND_ALL = "select * from contracts";
    private static final String INSERT =
            "insert into contracts (id, id_client, id_manager) values (?, ?, ?)";
    private static final String UPDATE =
            "update contracts set id_client = ?, id_manager = ? where id = ?";
    private static final String DELETE_BY_ID = "delete from contracts where id = ?";
    private static final String DELETE_ALL = "truncate table contracts";

    @Override
    public Long getCount() {
        try{
            return jdbcTemplate.queryForObject(COUNT, Long.class);
        } catch (DataAccessException e) {
            log.info("Error while request for the count of contracts table rows");
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
    public boolean checkIsInputRight(Contract contract) {
        return contract.getId() != null && contract.getId() > 0 &&
                contract.getIdClient() != null && contract.getIdClient() > 0 &&
                contract.getIdManager() != null && contract.getIdManager() > 0;
    }

    @Override
    public void create(Contract contract) {
        try {
            jdbcTemplate.update(INSERT,
                    contract.getId(), contract.getIdClient(), contract.getIdManager());
        } catch (DataAccessException e) {
            log.info("Error while creating db row of new contract: " + e.getMessage());
        }
    }

    @Override
    public Contract findById(final Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID + id, contractRowMapper);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public List<Contract> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL, contractRowMapper);
        } catch (DataAccessException e) {
            log.info("Error while finding all contracts in db: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Contract contract) {
        try {
            if (!checkIsInputRight(contract)) {
                throw new WrongInputException(contract.toString());
            }
            if (checkIsExistById(contract.getId())) {
                jdbcTemplate.update(UPDATE, contract.getIdClient(),
                        contract.getIdManager(), contract.getId());
            } else {
                create(contract);
            }
        } catch (DataAccessException e) {
            log.info("Error while updating the contract in db: " + e.getMessage());
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
            log.info("Error while deleting all rows of the contracts in db: " + e.getMessage());
        }
    }

    private static final RowMapper<Contract> contractRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idClient = row.getLong("id_client");
        Long idManager = row.getLong("id_manager");

        return new Contract(id, idClient, idManager);
    };
}