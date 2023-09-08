package com.example.manageNote.service;

import com.example.manageNote.model.Note;
import com.example.manageNote.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class NoteServiceImpl implements INoteService{

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
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

    @Override
    public String paramTojson(String paramIn) {
        if (paramIn.startsWith("{")) {
            log.info("Param already in Json format");
            return paramIn;
        }
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        return "{\"" + paramIn + "\"}";
    }


    @Override
    public Boolean checkPatient(String uri) {
        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uri);
        return restTemplate.getForObject(uri, Boolean.class);
    }
}
