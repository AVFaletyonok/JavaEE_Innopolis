package org.example.repository;

import org.example.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query(value = "SELECT 1", nativeQuery = true)
    int checkDatabaseConnection();

    @Query(value = "select count(*) from notes", nativeQuery = true)
    Long getNotesCount();
}
