package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.Note;
import org.example.repository.NoteRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final NoteRepository noteRepository;

    @PostConstruct
    public void init() {
        if (noteRepository.count() == 0) {
            Note note1 = new Note(null, "hometask1", "Eat breakfast",
                                    LocalDateTime.of(2025, 4, 14, 7, 30));
            Note note2 = new Note(null, "worktask1", "Work efficient",
                                    LocalDateTime.of(2025, 4, 14, 9, 00));
            Note note3 = new Note(null, "hometask2", "Walk home",
                                    LocalDateTime.of(2025, 4, 14, 18, 00));
            Note note4 = new Note(null, "hometask3", "Make dinner",
                                    LocalDateTime.of(2025, 4, 14, 19, 00));
            Note note5 = new Note(null, "hometask4", "Eat dinner",
                                    LocalDateTime.of(2025, 4, 14, 19, 30));
            noteRepository.saveAndFlush(note1);
            noteRepository.saveAndFlush(note2);
            noteRepository.saveAndFlush(note3);
            noteRepository.saveAndFlush(note4);
            noteRepository.saveAndFlush(note5);
        }
    }
}
