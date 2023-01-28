package com.example.manageNote.repository;

import com.example.manageNote.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, Integer> {
    List<Note> findByPatId(Integer patId);
}