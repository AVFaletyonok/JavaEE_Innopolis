package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderDTO;
import org.example.repositories.impl.OrderRepoImpl;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
public class OrderService {

    private final OrderRepoImpl orderRepo = new OrderRepoImpl();

    public boolean isDbConnected() {
        return orderRepo.isConnected();
    }

    public Long getOrdersCount() {
        Long ordersCount = orderRepo.getOrdersCount();
        return ordersCount;
    }

    public double getAverageAmountOfOrders() {
        double averageAmount = orderRepo.getAverageAmountOfOrders();
        return averageAmount;
    }

    public void createOrder(OrderDTO orderDTO) {
        long currentTimeMillis = System.currentTimeMillis();
        orderDTO.setTimestamp(new Timestamp(currentTimeMillis));
        Long id = getOrdersCount() + 1L;
        orderDTO.setId(id);
        orderRepo.insertOrder(orderDTO);
        log.info("New order inserted to db: " + orderDTO);
    }
}
