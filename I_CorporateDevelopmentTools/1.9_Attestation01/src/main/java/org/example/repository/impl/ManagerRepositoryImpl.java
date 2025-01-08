package org.example.repository.impl;

import org.example.config.JDBCTemplateConfig;
import org.example.entity.Manager;
import org.example.repository.ManagerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class ManagerRepositoryImpl implements ManagerRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String FIND_ALL = "select * from managers";
    private static final String INSERT =
            "insert into managers (id, first_name, last_name, position)" +
                          "values (?, ?, ?, ?)";
    private static final String UPDATE = "update managers set position = ? where id = ?";
    private static final String DELETE = "delete from managers where id = ?";

    @Override
    public List<Manager> findAll() {
        return jdbcTemplate.query(FIND_ALL, managerRowMapper);
    }

    @Override
    public void insert(Manager manager) {
        jdbcTemplate.update(INSERT,
                manager.getId(), manager.getFirstName(),
                manager.getLastName(), manager.getPosition());
    }

    @Override
    public void update(Manager manager, String newPosition) {
        jdbcTemplate.update(UPDATE, newPosition, manager.getId());
    }

    @Override
    public void delete(Manager manager) {
        jdbcTemplate.update(DELETE, manager.getId());
    }

    private static final RowMapper<Manager> managerRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String position = row.getString("position");

        return new Manager(id, firstName, lastName, position);
    };
}
