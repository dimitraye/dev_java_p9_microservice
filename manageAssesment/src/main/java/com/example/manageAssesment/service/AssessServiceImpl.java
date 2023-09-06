package com.example.manageAssesment.service;

import com.example.manageAssesment.model.MedicalTerms;
import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.RiskLevel;
import com.example.manageAssesment.model.Patient;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssessServiceImpl implements IAssesService{

    /*List<String> terms = List.of("Hémoglobine A1C", "Microalbumine", "Taille", "Poids", "Fumeur", "Anormal", "Anormal",
            "Vertige", "Rechute", "Réaction", "Anticorps");*/


    public static  final String NONE = "None";
    public static  final String BORDELINE = "Borderline";
    public static  final String IN_DANGER = "In danger";
    public static  final String EARLY_ONSET = "Early onset";
    public static  final String UNKNOWN = "Unknown";

    @Override
    public String evaluateRisk(Patient patient, List<Note> notes) {
        int nbFactors = nbTermsInNotes(notes);
        String riskLevel = null;

        //Cas 1

        boolean isNone = nbFactors == 0;
        //Cas 2
        boolean isBorderline = patient.isOlderthan30() && nbFactors  > 1;
        //Cas 3
        boolean isDanger1 = patient.isMale() && patient.isYougerthan30() && nbFactors > 2;
        boolean isDanger2 = patient.isFemale() && patient.isYougerthan30() && nbFactors > 3 ;
        boolean isDanger3 = patient.isOlderthan30() && nbFactors > 5;
        //Cas 4
        boolean isEarlyOnset1 = patient.isMale() && patient.isYougerthan30() && nbFactors > 4;
        boolean isEarlyOnset2 = patient.isFemale() && patient.isYougerthan30() && nbFactors > 6;
        boolean isEarlyOnset3 = patient.isOlderthan30() && nbFactors > 7;

        if (isNone){
            riskLevel = RiskLevel.NONE.getLabel();
        } else if (isEarlyOnset3) {
            riskLevel = RiskLevel.EARLY_ONSET.getLabel();
        } else if (isDanger3) {
            riskLevel = RiskLevel.IN_DANGER.getLabel();
        } else if (isBorderline) {
            riskLevel = RiskLevel.BORDERLINE.getLabel();
        } else if (isEarlyOnset1 || isEarlyOnset2) {
            riskLevel = RiskLevel.EARLY_ONSET.getLabel();
        } else if (isDanger1 || isDanger2) {
            riskLevel = RiskLevel.IN_DANGER.getLabel();
        } else {
            riskLevel = RiskLevel.UNKNOWN.getLabel();
        }

        return riskLevel;
    }

    @Override
    public String generateReport(Patient patient, String risk) {
        return "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + risk;
    }

    public int nbTermsInNotes(List<Note> notes){

        Set<String> termSet = new HashSet<>();

        //boucle pour trouver si un terme se trouve dans une liste note
        notes.forEach(note -> {
            MedicalTerms.getAllTerms().forEach(term -> {
                if (note.getContent().toLowerCase().contains(term.toLowerCase())) {
                    termSet.add(term);
                }
            });
        });
        return termSet.size();
    }



}
