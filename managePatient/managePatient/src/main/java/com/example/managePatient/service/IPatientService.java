package com.example.managePatient.service;

import com.example.managePatient.model.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    Patient save(Patient patient);

    Optional<Patient> findPatientById(Integer id);

    List<Patient> findAll();

    void delete(Integer id);

    List<Patient> findByGivenAndFamily(String given, String family);
}
