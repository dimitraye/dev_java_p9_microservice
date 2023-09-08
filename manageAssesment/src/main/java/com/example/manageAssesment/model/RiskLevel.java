package com.example.manageAssesment.model;

/**
 * Cette énumération représente les niveaux de risque possibles.
 */
public enum RiskLevel {
    NONE("None"),
    BORDERLINE("Borderline"),
    IN_DANGER("In danger"),
    EARLY_ONSET("Early onset"),
    UNKNOWN("Unknown");

    private final String label;

    RiskLevel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
