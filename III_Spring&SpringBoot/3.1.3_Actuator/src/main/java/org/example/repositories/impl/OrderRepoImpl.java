package org.example.repositories.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.config.JDBCTemplateConfig;
import org.example.dto.OrderDTO;
import org.example.repositories.OrderRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class OrderRepoImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.jdbcTemplate();

    private static final String ORDERS_COUNT = "select count(*) from orders";
    private static final String AVERAGE_AMOUNT_OF_ORDERS = "select avg(amount) from orders";
    private static final String INSERT =
            "insert into orders (id, product_number, count, amount, timestamp)" +
                        "values (?, ?, ?, ?, ?)";

    public List<OrderDTO> getOrdersList() {
        List<OrderDTO> orders = new ArrayList<>();
        return orders;
    }

    public boolean isConnected() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            if (connection != null) {
                return true;
            } else {
                log.info("Error while connection to db: connection is null.");
            }
        } catch (SQLException e) {
            log.info("Error while connection to db: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Long getOrdersCount() {
        return jdbcTemplate.queryForObject(ORDERS_COUNT, Long.class);
    }

    public double getAverageAmountOfOrders() {
        return jdbcTemplate.queryForObject(AVERAGE_AMOUNT_OF_ORDERS, double.class);
    }

    @Override
    public void insertOrder(OrderDTO orderDTO) {
        jdbcTemplate.update(INSERT,
                orderDTO.getId(), orderDTO.getProductNumber(),
                orderDTO.getCount(), orderDTO.getAmount(),
                orderDTO.getTimestamp());
    }
}
