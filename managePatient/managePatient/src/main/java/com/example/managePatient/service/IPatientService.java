package com.example.managePatient.service;

import com.example.managePatient.model.Patient;

import java.util.List;
import java.util.Optional;

/**
 * Interface that manage the interaction with the Patient entity
 */
public interface IPatientService {

    /**
     * Save the patient
     * @param patient
     * @return
     */
    Patient save(Patient patient);

    /**
     * Find a patient by its Id
     * @param id
     * @return a patient
     */
    Optional<Patient> findPatientById(Integer id);

    /**
     * Find all patients
     * @return a list of patient
     */
    List<Patient> findAll();

    /**
     * Delete a patient by its Id
     * @param id
     */
    void delete(Integer id);

    /**
     * Find a patient based on its firstName and lastName
     * @param given
     * @param family
     * @return a patient
     */
    List<Patient> findByGivenAndFamily(String given, String family);

    /**
     * Find all patients that have the same firstName and lastName
     * @param given
     * @param family
     * @return a list of patients
     */
    Patient findPatientByGivenAndFamily(String given, String family);

    /**
     * Convert a parameter into a Json text
     * @param paramIn
     * @return Json text
     */
    String paramTojson(String paramIn);

}
