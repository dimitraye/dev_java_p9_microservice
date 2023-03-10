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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class NoteController {


    @Autowired
    INoteService noteService;

    //@Autowired
    //ConfDockerService confDockerService;

    private static String baseUrl = "http://localhost";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";

    ObjectMapper mapper;

    //L'injection de dépendence peut se faire de pluisuers façons :
    //La première est en utilisant @Autowired
    //La deuxième est en mettant en paramètre dans le constructeur
    NoteController(ConfDockerService confDockerService) {
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
        String noteJson = noteService.paramTojson(body);
        //Convert json to Note object
        Note note = mapper.readValue(noteJson, Note.class);
        note.setCreationDate(new Date());

        // validation before save
        ResponseEntity<Object> errorResponse = noteService.getValidationErrors(note);
        if (errorResponse != null) {
            return errorResponse;
        }

        //TODO : Vérifie si l'id qu'on a reçu correspond à un patient
        //endpoint menant vers
        String uri = baseUrl + portPatient + endpointPatient + "/exist/" + note.getPatId();


        Boolean patientExist = noteService.checkPatient(uri);

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
        ResponseEntity<Object> errorResponse = noteService.getValidationErrors(note);
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


}

