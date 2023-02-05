package com.example.manageNote.service;

import com.example.manageNote.model.Note;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface INoteService {
    Note save(Note note);

    Optional<Note> findNoteById(Integer id);

    List<Note> findAll();

    void delete(Integer id);

    List<Note> findByPatId(Integer patId);

    String paramTojson(String paramIn);

    ResponseEntity<Object> getValidationErrors(Note note);

    Boolean checkPatient(String uri);
}
