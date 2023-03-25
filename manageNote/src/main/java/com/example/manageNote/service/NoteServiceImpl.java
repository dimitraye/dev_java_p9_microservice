package com.example.manageNote.service;

import com.example.manageNote.controller.NoteController;
import com.example.manageNote.model.Note;
import com.example.manageNote.repository.NoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
public class NoteServiceImpl implements INoteService{

    @Autowired
    NoteRepository noteRepository;

    Validator validator;

    NoteServiceImpl(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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
            //in this case it is already in json format
            return paramIn;
        }
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        return "{\"" + paramIn + "\"}";
    }

    @Override
    public ResponseEntity<Object> getValidationErrors(Note note) {
        Set<ConstraintViolation<Note>> violations = validator.validate(note);
        if (!violations.isEmpty()) {
            AtomicInteger nb = new AtomicInteger(1);
            JSONObject jsonError = new JSONObject();
            violations.stream().forEach(v -> {
                try {
                    jsonError.put("Error " + nb.getAndIncrement(), v.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            log.error(jsonError.toString());
            return new ResponseEntity<>(jsonError.toString(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public Boolean checkPatient(String uri) {
        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uri);
        return restTemplate.getForObject(uri, Boolean.class);
    }
}
