package com.example.manageAssesment.model;

import java.util.HashSet;
import java.util.Set;

public enum MedicalTerms {
    HEMOGLOBIN_A1C("Hémoglobine A1C"),
    MICROALBUMIN("Microalbumine"),
    HEIGHT("Taille"),
    WEIGHT("Poids"),
    SMOKER("Fumeur"),
    ABNORMAL("Anormal"),
    DIZZINESS("Vertige"),
    RELAPSE("Rechute"),
    REACTION("Réaction"),
    ANTIBODIES("Anticorps");

    private final String term;

    MedicalTerms(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public static Set<String> getAllTerms() {
        Set<String> terms = new HashSet<>();
        for (MedicalTerms term : values()) {
            terms.add(term.getTerm().toLowerCase());
        }
        return terms;
    }
}
