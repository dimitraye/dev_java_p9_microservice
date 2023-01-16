package com.example.managePatient.controller;

import com.example.managePatient.model.Patient;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class PatientController {
    @Autowired
    IPatientService patientService;

    Validator validator;

    ObjectMapper mapper;
    PatientController() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
       mapper.coercionConfigFor(LogicalType.Enum)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);


        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



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
    public ResponseEntity<Object> addPatient(@RequestBody String body) throws JsonProcessingException {

        //Convert request body to Json
        String patientJson = paramTojson(body);
        //Convert json to Patient object
        Patient patient = mapper.readValue(patientJson, Patient.class);

        // validation before save
        ResponseEntity<Object> errorResponse = getValidationErrors(patient);
        if (errorResponse != null) {
            return errorResponse;
        }
        log.info("Saving the new patient");

        return  new ResponseEntity<>(patientService.save(patient), HttpStatus.CREATED);
    }


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
        ResponseEntity<Object> errorResponse = getValidationErrors(patient);
        if (errorResponse != null) {
            return errorResponse;
        }

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


    public static String paramTojson(String paramIn) {
        if (paramIn.startsWith("{")) {
            //in this case it is already in json format
            return paramIn;
        }
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        return "{\"" + paramIn + "\"}";
    }


    private ResponseEntity<Object> getValidationErrors(Patient patient) {
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
