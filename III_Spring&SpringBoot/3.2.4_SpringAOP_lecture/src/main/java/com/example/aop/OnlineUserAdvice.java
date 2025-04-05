package com.example.aop;

import com.example.counter.OnlineUserCounter;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class OnlineUserAdvice implements MethodBeforeAdvice {
    private final OnlineUserCounter counter;

    public OnlineUserAdvice(OnlineUserCounter counter) {
        this.counter = counter;
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        counter.increment();
    }
}
