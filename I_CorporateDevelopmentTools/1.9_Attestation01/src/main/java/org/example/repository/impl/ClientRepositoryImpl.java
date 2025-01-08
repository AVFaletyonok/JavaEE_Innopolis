package org.example.repository.impl;

import org.example.config.JDBCTemplateConfig;
import org.example.entity.Client;
import org.example.repository.ClientRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String FIND_ALL = "select * from clients";
    private static final String INSERT =
            "insert into clients (id, first_name, last_name, passport_number, phone_number)" +
                         "values (?, ?, ?, ?, ?)";
    private static final String UPDATE = "update clients set phone_number = ? where id = ?";
    private static final String DELETE = "delete from clients where id = ?";

    @Override
    public List<Client> findAll() {
        return jdbcTemplate.query(FIND_ALL, clientRowMapper);
    }

    @Override
    public void insert(Client client) {
        jdbcTemplate.update(INSERT,
                client.getId(), client.getFirstName(), client.getLastName(),
                client.getPassportNumber(), client.getPhoneNumber());
    }

    @Override
    public void updatePhoneNumber(Client client, String newPhoneNumber) {
        jdbcTemplate.update(UPDATE, newPhoneNumber, client.getId());
    }

    @Override
    public void delete(Client client) {
        jdbcTemplate.update(DELETE, client.getId());
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
