package org.example.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.example.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomActuator {

    @Bean
    public String hello(MeterRegistry meterRegistry) {
        meterRegistry.gauge("custom", 1);
        return "hello";
    }

    @Bean
    public MeterBinder bindOrdersCount(OrderService orderService) {
        Tag globalTag = Tag.of("business", "business");
        return meterRegistry -> meterRegistry.gauge("count_of_orders", List.of(globalTag), orderService.getOrdersCount());
    }

    @Bean
    public MeterBinder bindAverageAmountOfOrders(OrderService orderService) {
        Tag globalTag = Tag.of("business", "business");
        return meterRegistry -> meterRegistry.gauge("average_amount_of_orders", List.of(globalTag), orderService.getAverageAmountOfOrders());
    }

//    @Bean
//    public MeterBinder meterBinder(OrderRepoImpl orderRepo) {
//        return meterRegistry ->
//                Gauge.builder("orders.count", orderRepo::getOrdersCount)
//                        .register(meterRegistry);
//    }
}
