package com.example.manageNote.controller;


import com.example.manageNote.model.Note;
import com.example.manageNote.service.ConfDockerService;
import com.example.manageNote.service.INoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Manage the requests linked to a Note
 */
@Slf4j
@RestController
public class NoteController {


    private final INoteService noteService;
    private final ConfDockerService confDockerService;
    private final ObjectMapper mapper;

    private static String baseUrl = "http://localhost";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";

    private final Validator validator;


    public NoteController(INoteService noteService, ConfDockerService confDockerService) {
        this.noteService = noteService;
        this.confDockerService = confDockerService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.coercionConfigFor(LogicalType.Enum)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        if (confDockerService.isDocker()) {
            baseUrl = "http://host.docker.internal";
            log.info("base_url in notecontroller " + baseUrl);
        }
    }


    /**
     * Find all the notes when calling this endpoint
     * @return a list of notes
     */
    @GetMapping("/notes")
    public List<Note> findAllNotes() {
        return noteService.findAll();
    }

    /**
     *  Find all notes of a patient by using the patient's Id
     * @param patId
     * @return a list of notes
     */
    @GetMapping("/patHistory")
    public ResponseEntity<List<Note>> findAllByPatId(@RequestParam Integer patId){
        List<Note> notes = noteService.findByPatId(patId);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    /**
     * Find a note based on its Id when calling this endpoint
     * @param id
     * @return the page that present the details of the note
     */
    @GetMapping("/note/{id}")
    public ResponseEntity<Note> find(@PathVariable Integer id){
        Note noteFromDB = noteService.findNoteById(id).orElse(null);

        if(noteFromDB == null) {
            log.error("Error : id Note doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Returning the note's informations");
        return new ResponseEntity<>(noteFromDB, HttpStatus.OK);
    }


    /**
     * Manage the creation of a note when calling this endpoint
     * @param body
     * @return the page that allow to create a note
     * @throws JsonProcessingException
     */
    @PostMapping("/patHistory/add")
    public ResponseEntity<Object> addNote(@RequestBody String body) throws JsonProcessingException {

        log.info("Converting request body to Json");
        String noteJson = noteService.paramTojson(body);
        log.info("Converting json to Note object");
        Note note = mapper.readValue(noteJson, Note.class);
        note.setCreationDate(new Date());

        log.info("Validation before save");
        ResponseEntity<Object> errorResponse = getValidationErrors(note);
        if (errorResponse != null) {
            return errorResponse;
        }

        String uri = baseUrl + portPatient + endpointPatient + "/exist/" + note.getPatId();


        Boolean patientExist = noteService.checkPatient(uri);

        if (!patientExist) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Saving the new note");
        return  new ResponseEntity<>(noteService.save(note), HttpStatus.CREATED);
    }


    /**
     * Manage the update of a note when calling this endpoint
     * @param id
     * @param note
     * @return the page that allow to update a note
     */
    @PutMapping("/note/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody Note note) {

        Note noteFromDB = noteService.findNoteById(id).get();

        if(noteFromDB == null) {
            log.error("Error : Note already  exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.debug("Set properties in the object noteFromDB.");
        noteFromDB.setContent(note.getContent());


        log.info("Validation before save");
        ResponseEntity<Object> errorResponse = getValidationErrors(note);
        if (errorResponse != null) {
            return errorResponse;
        }

        log.info("Updating a note");
        return new ResponseEntity<>(noteService.save(noteFromDB), HttpStatus.OK);
    }


    /**
     * Manage the erasure of a note when calling this endpoint
     * @param id
     * @return the notes page
     */
    @DeleteMapping("/note/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id){


        try {
            noteService.delete(id);
            log.info("Person deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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

}

