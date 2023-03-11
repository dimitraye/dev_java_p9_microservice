package com.example.manageAssesment.service;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;

import java.util.List;

/**
 * Interface that manage the interaction with the Assessment entity
 */
public interface IAssesService {

    /**
     * Evaluate the risk of diabetese of the patient based on notes
     * and his informations.
     * @param patient
     * @param notes
     * @return String
     */
    String evaluateRisk(Patient patient,List<Note> notes);

    /**
     * Geberate a report based on the risk of diabetese and the
     * patient's informations.
     * @param patient
     * @param risk
     * @return a report
     */
    String generateReport(Patient patient, String risk);


}
