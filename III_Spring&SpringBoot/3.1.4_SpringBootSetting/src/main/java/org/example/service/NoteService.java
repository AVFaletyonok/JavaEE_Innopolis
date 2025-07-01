package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Note;
import org.example.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public boolean isDatabaseConnected() {
        try {
            return noteRepository.checkDatabaseConnection() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getNotesCount() {
        Long ordersCount = noteRepository.getNotesCount();
        return ordersCount;
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNote(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        note.setId(null);
        note.setCreatedAt(LocalDateTime.now());
        Note savedNote = noteRepository.saveAndFlush(note);
        return savedNote;
    }

    public Optional<Note> updateNote(Long id, Note note) {
        Optional<Note> noteDB = noteRepository.findById(id);
        if (noteDB.isEmpty()) {
            return Optional.empty();
        }
        note.setId(noteDB.get().getId());
        note.setCreatedAt(LocalDateTime.now());
        Note savedNote = noteRepository.saveAndFlush(note);
        return Optional.of(savedNote);
    }

    public Optional<Note> deleteNote(Long id) {
        Optional<Note> deletedNote = noteRepository.findById(id);
        if (deletedNote.isPresent()) {
            noteRepository.deleteById(id);
        }
        return deletedNote;
    }
}
