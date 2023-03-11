package com.example.manageAssesment.service;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;

import java.util.List;

/**
 * Interface that manage the interaction with the Assessment entity
 */
public interface IAssesService {

    String evaluateRisk(Patient patient,List<Note> notes);

    String generateReport(Patient patient, String risk);


}
