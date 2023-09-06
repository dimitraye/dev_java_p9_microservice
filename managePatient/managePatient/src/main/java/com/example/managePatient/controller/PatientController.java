package com.example.managePatient.controller;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import com.example.managePatient.service.IPatientService;
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

import java.util.List;

/**
 * Manage the requests linked to a Patient
 */
@Slf4j
@RestController
public class PatientController {
    private final PatientRepository patientRepository;
    @Autowired
    IPatientService patientService;


    ObjectMapper mapper;
    PatientController(PatientRepository patientRepository) {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
       mapper.coercionConfigFor(LogicalType.Enum)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);



        this.patientRepository = patientRepository;
    }


    /**
     * Find all the patients when calling this endpoint
     * @return a list of patients
     */
    @GetMapping("/patients")
    public List<Patient> findAllPatients() {
        return patientService.findAll();
    }

    /**
     * Find a patient based on its Id when calling this endpoint
     * if the patient does exist in the Data Base.
     * @param id
     * @return patient
     */
    @GetMapping("/patient/{id}")
    public ResponseEntity<Patient> find(@PathVariable Integer id){
        Patient patientFromDB = patientService.findPatientById(id).orElse(null);

        //If the patient doesn't exist, send status 404
        if(patientFromDB == null) {
            //log.error("Error : id Patient doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //log.info("Returning the patient's informations");
        //else, return patient
        return new ResponseEntity<>(patientFromDB, HttpStatus.OK);
    }

    /**
     * Check if a patient exist based on it's Id
     * @param id
     * @return boolean
     */
    @GetMapping("/patient/exist/{id}")
    public ResponseEntity<Boolean> patientExist(@PathVariable Integer id){
        //Récupère un patient dans la BD à partir de son ID. sinon renvoie null
        Patient patientFromDB = patientService.findPatientById(id).orElse(null);

        //S'il n'éxiste pas, envoie statut 404
        if(patientFromDB == null) {
            //log.error("Error : id Patient doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //log.info("Returning the patient's informations");
        //Sinon, retourner boolean et status de la requête
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     * Manage to show all patients that have the same FirstName and LastName
     * @param given
     * @param family
     * @return a list of patients
     */
    @GetMapping("/patients/givenfamily")
    public ResponseEntity<List<Patient>> patientsByGivenAndFamily(@RequestParam String given, @RequestParam String family){
        List<Patient> patients = patientService.findByGivenAndFamily(given, family);

        //log.info("Returning the patient's informations");
        //Sinon, retourner patient
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    /**
     * Manage to show a patient based on its firstName and lastName
     * @param given
     * @param family
     * @return a patient
     */
    @GetMapping("/patient/givenfamily")
    public ResponseEntity<Patient> patientByGivenAndFamily(@RequestParam String given, @RequestParam String family){
        Patient patient = patientService.findPatientByGivenAndFamily(given, family);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //log.info("Returning the patient's informations");
        //Sinon, retourner patient
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Manage the creation of a patient when calling this endpoint
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/patient/add")
    public ResponseEntity<Object> addPatient(@RequestBody String body) throws JsonProcessingException {

        //Convert request body to Json
        String patientJson = patientService.paramTojson(body);
        //Convert json to Patient object
        Patient patient = mapper.readValue(patientJson, Patient.class);

        // validation before save
        ResponseEntity<Object> errorResponse = patientService.getValidationErrors(patient);
        if (errorResponse != null) {
            return errorResponse;
        }
        log.info("Saving the new patient");

        return  new ResponseEntity<>(patientService.save(patient), HttpStatus.CREATED);
    }

    /**
     * Manage the update of a patient when calling this endpoint
     * @param id
     * @param patient
     * @return
     */
    @PutMapping("/patient/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody Patient patient) {

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


        // validation before update
        ResponseEntity<Object> errorResponse = patientService.getValidationErrors(patient);
        if (errorResponse != null) {
            return errorResponse;
        }

        log.info("Updating a patient");
        return new ResponseEntity<>(patientService.save(patientFromDB), HttpStatus.OK);
    }

    /**
     * Manage the erasure of a patient when calling this endpoint
     * @param id
     * @return nothing
     */
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
