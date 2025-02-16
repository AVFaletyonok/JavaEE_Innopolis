package org.example.actuator;

import lombok.extern.slf4j.Slf4j;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


@Slf4j
@Component("HealthIndicatorDB")
public class HealthIndicatorDB implements HealthIndicator {

    @Autowired
    private OrderService orderService;

    @Override
    public Health health() {
        if (orderService.isDbConnected()) {
            return Health.up()
                    .withDetail("message", "PostgreSQL is up and running")
                    .build();
        }
        return Health.down()
                .withDetail("message", "PostgreSQL connection failed")
                .build();
    }
}
