package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.exception.RowWithIdDontExistsException;
import org.example.exception.WrongInputException;
import org.example.model.Client;
import org.example.repository.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Slf4j
public class ClientRepositoryImpl implements Repository<Client> {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String COUNT = "select count(id) from clients";
    private static final String CHECK_IS_EXIST_BY_ID =
            "select count(id) from clients where id = ";
    private static final String FIND_BY_ID = "select * from clients where id = ";
    private static final String FIND_ALL = "select * from clients";
    private static final String INSERT =
            "insert into clients (id, first_name, last_name, passport_number, phone_number)" +
                         "values (?, ?, ?, ?, ?)";
    private static final String UPDATE = "update clients set first_name = ?, last_name = ?, " +
                                         "passport_number = ?, phone_number = ? where id = ?";
    private static final String DELETE_BY_ID = "delete from clients where id = ?";
    private static final String DELETE_ALL = "truncate table clients";

    @Override
    public Long getCount() {
        try{
            return jdbcTemplate.queryForObject(COUNT, Long.class);
        } catch (DataAccessException e) {
            log.info("Error while request for the count of clients table rows");
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
    public boolean checkIsInputRight(Client client) {
        return client.getId() != null && client.getId() > 0 &&
                client.getFirstName() != null && client.getLastName() != null &&
                client.getPassportNumber() != null && client.getPhoneNumber() != null;
    }

    @Override
    public void create(Client client) {
        try {
            jdbcTemplate.update(INSERT,
                    client.getId(), client.getFirstName(), client.getLastName(),
                    client.getPassportNumber(), client.getPhoneNumber());
        } catch (DataAccessException e) {
            log.info("Error while creating db row of new client: " + e.getMessage());
        }
    }

    @Override
    public Client findById(final Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID + id, clientRowMapper);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public List<Client> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL, clientRowMapper);
        } catch (DataAccessException e) {
            log.info("Error while finding all clients in db: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Client client) {
        try {
            if (!checkIsInputRight(client)) {
                throw new WrongInputException(client.toString());
            }
            if (checkIsExistById(client.getId())) {
                jdbcTemplate.update(UPDATE, client.getFirstName(), client.getLastName(),
                        client.getPassportNumber(), client.getPhoneNumber(), client.getId());
            } else {
                create(client);
            }
        } catch (DataAccessException e) {
            log.info("Error while updating the client in db: " + e.getMessage());
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
            log.info("Error while deleting all rows of the clients in db: " + e.getMessage());
        }
    }

    private static final RowMapper<Client> clientRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String passportNumber = row.getString("passport_number");
        String phoneNumber = row.getString("phone_number");

        return new Client(id, firstName, lastName, passportNumber, phoneNumber);
    };
}
