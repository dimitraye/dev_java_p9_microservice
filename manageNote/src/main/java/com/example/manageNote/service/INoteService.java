package com.example.manageNote.service;

import com.example.manageNote.model.Note;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interface that manage the interaction with the Note entity
 */
public interface INoteService {

    /**
     *Save the note
     * @param note
     * @return the note to save
     */
    Note save(Note note);

    /**
     * Find the note by its id
     * @param id
     * @return
     */
    Optional<Note> findNoteById(Integer id);

    /**
     * Find all notes
     * @return a list of notes
     */
    List<Note> findAll();

    /**
     * Delete a note
     * @param id
     */
    void delete(Integer id);

    /**
     * Find all notes of a patient by the patient's id
     * @param patId
     * @return a list of notes
     */
    List<Note> findByPatId(Integer patId);

    /**
     * Convert a parameter into a Json text
     * @param paramIn
     * @return JSon text
     */
    String paramTojson(String paramIn);


    /**
     *
     * @param uri
     * @return
     */
    Boolean checkPatient(String uri);
}
