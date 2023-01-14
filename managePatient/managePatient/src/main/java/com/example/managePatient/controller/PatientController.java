package com.example.managePatient.controller;

import com.example.managePatient.model.Patient;
import com.example.managePatient.service.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class PatientController {
    @Autowired
    IPatientService patientService;

    @GetMapping("/patients")
    public List<Patient> findAllPatients() {
        return patientService.findAll();
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> find(@PathVariable Integer id){
        Patient patientFromDB = patientService.findPatientById(id).orElse(null);

        //S'il n'éxiste pas, envoie statut 404
        if(patientFromDB == null) {
            //log.error("Error : id Patient doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //log.info("Returning the patient's informations");
        //Sinon, retourner patient
        return new ResponseEntity<>(patientFromDB, HttpStatus.OK);
    }

    @PostMapping("/patient/add")
    public ResponseEntity<Patient> addPatient(@RequestBody @Valid Patient patient, BindingResult result) {

        if(result.hasErrors()) {
            //log.error("Error : ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //3 - Else save
        //log.info("Saving the new patient");

        return  new ResponseEntity<>(patientService.save(patient), HttpStatus.CREATED);
    }


    @PutMapping("/patient/{id}")
    public ResponseEntity<Patient> update(@PathVariable Integer id, @RequestBody @Valid Patient patient) {

        //Cherche person par son id
        Patient patientFromDB = patientService.findPatientById(id).get();

        if(patientFromDB == null) {
            //log.error("Error : Patient already  exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //log.debug("Set properties in the object patientFromDB.");
        //sinon mettre à jour person
        patientFromDB.setAddress(patient.getAddress());
        patientFromDB.setGiven(patient.getGiven());
        patientFromDB.setFamily(patient.getFamily());
        patientFromDB.setPhone(patient.getPhone());
        patientFromDB.setSex(patient.getSex());
        patientFromDB.setDob(patient.getDob());


        log.info("Updating a patient");
        return new ResponseEntity<>(patientService.save(patientFromDB), HttpStatus.OK);
    }


    @DeleteMapping("/patient/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Integer id){


        try {
            patientService.delete(id);
            log.info("Person deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
