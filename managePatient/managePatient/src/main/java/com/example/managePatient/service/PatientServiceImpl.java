package com.example.managePatient.service;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class PatientServiceImpl implements IPatientService{
    private final PatientRepository patientRepository;



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
            log.info("Param already in Json format");
            return paramIn;
        }
        paramIn = paramIn.replaceAll("=", "\":\"");
        paramIn = paramIn.replaceAll("&", "\",\"");
        return "{\"" + paramIn + "\"}";
    }


}
