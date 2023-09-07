package com.example.managePatient.service;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class PatientServiceImpl implements IPatientService{
    private final PatientRepository patientRepository;
    private final Validator validator;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> findPatientById(Integer id) {
        return patientRepository.findById(id);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        patientRepository.deleteById(id);
    }


    @Override
    public List<Patient> findByGivenAndFamily(String given, String family) {
        return patientRepository.findByGivenIgnoreCaseAndFamilyIgnoreCase(given, family);
    }

    @Override
    public Patient findPatientByGivenAndFamily(String given, String family) {
         List<Patient> patients = findByGivenAndFamily(given, family);

         if (!CollectionUtils.isEmpty(patients)) {
             return patients.get(0);
         }
        return null;
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
