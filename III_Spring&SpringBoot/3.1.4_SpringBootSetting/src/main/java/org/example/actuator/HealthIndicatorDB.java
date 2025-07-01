package org.example.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("production")
@Slf4j
@Component("HealthIndicatorDB")
@RequiredArgsConstructor
public class HealthIndicatorDB implements HealthIndicator {

    private NoteService noteService;

    @Override
    public Health health() {
        if (noteService.isDatabaseConnected()) {
            return Health.up()
                    .withDetail("message", "PostgreSQL is up and running")
                    .build();
        }
        return Health.down()
                .withDetail("message", "PostgreSQL connection failed")
                .build();
    }
}