package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Note;
import org.example.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/home")
    public String getHome() {
        return "Welcome from spring module 3.1.4 Spring Boot Setting!";
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathVariable Long id) {
        Optional<Note> note = noteService.getNote(id);
        return note.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(note.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note savedNote = noteService.createNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id,
                                           @RequestBody Note note) {
        Optional<Note> savedNote = noteService.updateNote(id, note);
        return savedNote.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(savedNote.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteNote(@PathVariable Long id) {
        Optional<Note> deletedNote = noteService.deleteNote(id);

        return deletedNote.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(deletedNote.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
