package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.exception.RowWithIdDontExistsException;
import org.example.exception.WrongInputException;
import org.example.model.Manager;
import org.example.repository.Repository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Slf4j
public class ManagerRepositoryImpl implements Repository<Manager> {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String COUNT = "select count(id) from managers";
    private static final String CHECK_IS_EXIST_BY_ID =
            "select count(id) from managers where id = ";
    private static final String FIND_BY_ID = "select * from managers where id = ";
    private static final String FIND_ALL = "select * from managers";
    private static final String INSERT =
            "insert into managers (id, first_name, last_name, position)" +
                          "values (?, ?, ?, ?)";
    private static final String UPDATE =
            "update managers set first_name = ?, last_name = ?, position = ? where id = ?";
    private static final String DELETE_BY_ID = "delete from managers where id = ?";
    private static final String DELETE_ALL = "truncate table managers";

    @Override
    public Long getCount() {
        try{
            return jdbcTemplate.queryForObject(COUNT, Long.class);
        } catch (DataAccessException e) {
            log.info("Error while request for the count of managers table rows");
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
    public boolean checkIsInputRight(Manager manager) {
        return manager.getId() != null && manager.getId() > 0 &&
                manager.getFirstName() != null && manager.getLastName() != null &&
                manager.getPosition() != null;
    }

    @Override
    public void create(Manager manager) {
        try {
            jdbcTemplate.update(INSERT,
                    manager.getId(), manager.getFirstName(),
                    manager.getLastName(), manager.getPosition());
        } catch (DataAccessException e) {
            log.info("Error while creating db row of new manager: " + e.getMessage());
        }
    }

    @Override
    public Manager findById(final Long id) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID + id, managerRowMapper);
        } catch (DataAccessException e) {
            throw new RowWithIdDontExistsException(this.getClass().getName(), id, e);
        }
    }

    @Override
    public List<Manager> findAll() {
        try {
            return jdbcTemplate.query(FIND_ALL, managerRowMapper);
        } catch (DataAccessException e) {
            log.info("Error while finding all managers in db: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Manager manager) {
        try {
            if (!checkIsInputRight(manager)) {
                throw new WrongInputException(manager.toString());
            }
            if (checkIsExistById(manager.getId())) {
                jdbcTemplate.update(UPDATE, manager.getFirstName(), manager.getLastName(),
                        manager.getPosition(), manager.getId());
            } else {
                create(manager);
            }
        } catch (DataAccessException e) {
            log.info("Error while updating the manager in db: " + e.getMessage());
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
            log.info("Error while deleting all rows of the managers in db: " + e.getMessage());
        }
    }

    private static final RowMapper<Manager> managerRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String position = row.getString("position");

        return new Manager(id, firstName, lastName, position);
    };
}
