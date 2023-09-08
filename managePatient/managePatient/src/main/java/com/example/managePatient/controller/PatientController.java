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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manage the requests linked to a Patient
 */
@Slf4j
@RestController

public class PatientController {
    private final PatientRepository patientRepository;
    private final IPatientService patientService;
    private final ObjectMapper mapper;

    private final Validator validator;


    public PatientController(PatientRepository patientRepository, IPatientService patientService) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.coercionConfigFor(LogicalType.Enum)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

        if(patientFromDB == null) {
            log.error("Error : id Patient doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Returning the patient's informations");
        return new ResponseEntity<>(patientFromDB, HttpStatus.OK);
    }

    /**
     * Check if a patient exist based on it's Id
     * @param id
     * @return boolean
     */
    @GetMapping("/patient/exist/{id}")
    public ResponseEntity<Boolean> patientExist(@PathVariable Integer id){
        Patient patientFromDB = patientService.findPatientById(id).orElse(null);

        if(patientFromDB == null) {
            log.error("Error : id Patient doesn't exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Returning the patient's informations");
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

        log.info("Returning the patient's informations");
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
        log.info("Returning the patient's informations");
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

        String patientJson = patientService.paramTojson(body);
        Patient patient = mapper.readValue(patientJson, Patient.class);

        log.info("Validation before save");
        ResponseEntity<Object> errorResponse = getValidationErrors(patient);
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

        Patient patientFromDB = patientService.findPatientById(id).get();

        if(patientFromDB == null) {
            log.error("Error : Patient already  exist in the Data Base.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.debug("Set properties in the object patientFromDB.");
        patientFromDB.setAddress(patient.getAddress());
        patientFromDB.setGiven(patient.getGiven());
        patientFromDB.setFamily(patient.getFamily());
        patientFromDB.setPhone(patient.getPhone());
        patientFromDB.setSex(patient.getSex());
        patientFromDB.setDob(patient.getDob());


        log.info("validation before update");
        ResponseEntity<Object> errorResponse = getValidationErrors(patient);
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



    public ResponseEntity<Object> getValidationErrors(Patient patient) {
        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
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
