package com.example.manageAssesment.service;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;

import java.util.List;

public interface IAssesService {

    String evaluateRisk(Patient patient,List<Note> notes);

    String generateReport(Patient patient, String risk);


}
