package com.example.manageNote.repository;

import com.example.manageNote.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Manage database operations for a Note entity
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, Integer> {

    /**
     * Find all notes that have the same patId
     * @param patId
     * @return a list of notes
     */
    List<Note> findByPatId(Integer patId);
}