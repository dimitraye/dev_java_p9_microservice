package com.example.manageNote.service;

import com.example.manageNote.model.Note;
import com.example.manageNote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements INoteService{

    @Autowired
    NoteRepository noteRepository;

    @Override
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> findNoteById(Integer id) {
        return noteRepository.findById(id);
    }

    @Override
    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        noteRepository.deleteById(id);
    }

    @Override
    public List<Note> findByPatId(Integer patId) {
        return noteRepository.findByPatId(patId);
    }
}
