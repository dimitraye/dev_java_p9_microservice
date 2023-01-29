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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class NoteController {


    @Autowired
    INoteService noteService;

    //@Autowired
    //ConfDockerService confDockerService;

    Validator validator;

    private static String baseUrl = "http://localhost";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";

    ObjectMapper mapper;
    NoteController(ConfDockerService confDockerService) {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.coercionConfigFor(LogicalType.Enum)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        if (confDockerService.isDocker()) {
            baseUrl = "http://host.docker.internal";
            log.info("base_url in notecontroller " + baseUrl);
        }
    }



    @GetMapping("/notes")
    public List<Note> findAllNotes() {
        return noteService.findAll();
    }

    @GetMapping("/patHistory")
    public ResponseEntity<List<Note>> findAllByPatId(@RequestParam Integer patId){
        List<Note> notes = noteService.findByPatId(patId);
        return new ResponseEntity<>(notes, HttpStatus.OK);


    }

    @GetMapping("/note/{id}")
    public ResponseEntity<Note> find(@PathVariable Integer id){
        Note noteFromDB = noteService.findNoteById(id).orElse(null);

        //S'il n'éxiste pas, envoie statut 404
        if(noteFromDB == null) {
            //log.error("Error : id Note doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //log.info("Returning the note's informations");
        //Sinon, retourner note
        return new ResponseEntity<>(noteFromDB, HttpStatus.OK);
    }



    @PostMapping("/patHistory/add")
    public ResponseEntity<Object> addNote(@RequestBody String body) throws JsonProcessingException {

        //Convert request body to Json
        String noteJson = paramTojson(body);
        //Convert json to Note object
        Note note = mapper.readValue(noteJson, Note.class);
        note.setCreationDate(new Date());

        // validation before save
        ResponseEntity<Object> errorResponse = getValidationErrors(note);
        if (errorResponse != null) {
            return errorResponse;
        }

        //TODO : Vérifi si l'id qu'on a reçu correspond à un patient
        //endpoint menant vers
        String uri = baseUrl + portPatient + endpointPatient + "/exist/" + note.getPatId();

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uri);
        Boolean patientExist = restTemplate.getForObject(uri, Boolean.class);

        //si patient n'existe pas
        if (!patientExist) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Saving the new note");
        return  new ResponseEntity<>(noteService.save(note), HttpStatus.CREATED);
    }


    @PutMapping("/note/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody Note note) {

        //Cherche person par son id
        Note noteFromDB = noteService.findNoteById(id).get();

        if(noteFromDB == null) {
            //log.error("Error : Note already  exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //log.debug("Set properties in the object noteFromDB.");
        //sinon mettre à jour person
        noteFromDB.setContent(note.getContent());


        // validation before update
        ResponseEntity<Object> errorResponse = getValidationErrors(note);
        if (errorResponse != null) {
            return errorResponse;
        }

        log.info("Updating a note");
        return new ResponseEntity<>(noteService.save(noteFromDB), HttpStatus.OK);
    }


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


    public static String paramTojson(String paramIn) {
        if (paramIn.startsWith("{")) {
            //in this case it is already in json format
            return paramIn;
        }
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        return "{\"" + paramIn + "\"}";
    }


    private ResponseEntity<Object> getValidationErrors(Note note) {
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

