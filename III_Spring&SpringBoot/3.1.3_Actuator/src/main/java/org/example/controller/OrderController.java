package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderDTO;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private ProductContoller productContoller;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/v1")
    public String getOrder() {
        log.info("Said hello.");
        return "Hello, human!";
    }

    @PostMapping("api/v1/withparams")
    public String postOrderTest(@RequestParam String param1) {
        log.info("I get next params: " + param1);
        return "List of all params: " + param1;
    }

    @GetMapping("/api/v1/getorderscount")
    public String getOrdersCount() {
        Long count = orderService.getOrdersCount();
        log.info("Orders count: " + count);
        return "Orders count: " + count;
    }

    @PostMapping("/api/v1/addorder")
    public ResponseEntity<String> addOrder(@RequestBody OrderDTO orderDTO) {
        if (orderDTO.getCount() == null || orderDTO.getAmount() == null ||
                orderDTO.getCount() <= 0 ||
                orderDTO.getAmount().compareTo(new BigDecimal(0)) <= 0 ||
                orderDTO.getProductNumber() > productContoller.getProductService().getProductsCount()) {
            log.info("Bad request was detected: " + orderDTO);
            return ResponseEntity.badRequest()
                    .body("Count and amount have to be greater than 0, " +
                            "productNumber have to be in productList");
        }
        orderService.createOrder(orderDTO);
        return ResponseEntity.ok("Order is created successfully");
    }
}
