package com.example.manageAssesment.model;

/**
 * This Enum represents the possible risk levels
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
