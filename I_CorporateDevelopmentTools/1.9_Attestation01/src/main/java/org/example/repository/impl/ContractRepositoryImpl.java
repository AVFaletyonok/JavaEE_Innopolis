package org.example.repository.impl;

import org.example.config.JDBCTemplateConfig;
import org.example.entity.Contract;
import org.example.repository.ContractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class ContractRepositoryImpl implements ContractRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String FIND_ALL = "select * from contracts";
    private static final String INSERT = "insert into contracts (id, id_client, id_manager) values (?, ?, ?)";
    private static final String UPDATE = "update contracts set id_manager = ? where id = ?";
    private static final String DELETE = "delete from contracts where id = ?";

    @Override
    public List<Contract> findAll() {
        return jdbcTemplate.query(FIND_ALL, contractRowMapper);
    }

    @Override
    public void insert(Contract contract) {
        jdbcTemplate.update(INSERT,
                contract.getId(), contract.getIdClient(), contract.getIdManager());
    }

    @Override
    public void update(Contract contract, Long newIdManager) {
        jdbcTemplate.update(UPDATE, newIdManager, contract.getId());
    }

    @Override
    public void delete(Contract contract) {
        jdbcTemplate.update(DELETE, contract.getId());
    }

    private static final RowMapper<Contract> contractRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        Long idClient = row.getLong("id_client");
        Long idManager = row.getLong("id_manager");

        return new Contract(id, idClient, idManager);
    };
}