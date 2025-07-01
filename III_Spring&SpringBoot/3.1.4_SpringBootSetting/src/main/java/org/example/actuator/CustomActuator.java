package org.example.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.example.service.NoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("production")
@Component
public class CustomActuator {

    @Bean
    public String hello(MeterRegistry meterRegistry) {
        meterRegistry.gauge("custom", 1);
        return "hello";
    }

    @Bean
    public MeterBinder bindNotesCount(NoteService noteService) {
        Tag globalTag = Tag.of("business", "business");
        return meterRegistry -> meterRegistry.gauge("count_of_notes", List.of(globalTag), noteService.getNotesCount());
    }
}