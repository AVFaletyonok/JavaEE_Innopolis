package com.example.service.impl;

import com.example.service.Service;

public class SimpleService implements Service {
    @Override
    public void doSomething() {
        System.out.println("Method 'doSomething' is executing.");
    }
}
