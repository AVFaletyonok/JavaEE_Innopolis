package com.example.configuration;

import com.example.aop.LoggingAdvice;
import com.example.aop.OnlineUserAdvice;
import com.example.controller.UserCounterController;
import com.example.counter.OnlineUserCounter;
import com.example.service.Service;
import com.example.service.impl.SimpleService;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Service service() {
        ProxyFactory proxyFactory = new ProxyFactory(new SimpleService());
        proxyFactory.addAdvice(new LoggingAdvice());
        return (Service) proxyFactory.getProxy();
    }

    @Bean
    public OnlineUserCounter onlineUserCounter() {
        return new OnlineUserCounter();
    }

    @Bean
    public UserCounterController userCounterController(OnlineUserCounter counter) {
        ProxyFactory proxyFactory = new ProxyFactory(new UserCounterController(counter));
        proxyFactory.addAdvice(new OnlineUserAdvice(counter));
        return (UserCounterController) proxyFactory.getProxy();
    }
}
