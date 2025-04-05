package com.example.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class OnlineUserCounter {
    private final AtomicInteger onlineUsers = new AtomicInteger(0);

    public void increment() {
        onlineUsers.incrementAndGet();
    }

    public int getOnlineUsers() {
        return onlineUsers.get();
    }
}
