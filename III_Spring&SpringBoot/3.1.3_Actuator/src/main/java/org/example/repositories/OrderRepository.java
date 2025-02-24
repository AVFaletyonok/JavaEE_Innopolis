package org.example.repositories;

import org.example.dto.OrderDTO;

public interface OrderRepository {

    Long getOrdersCount();

    void insertOrder(OrderDTO orderDTO);
}
