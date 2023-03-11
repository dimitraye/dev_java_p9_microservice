package com.example.manageAssesment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model that represent the genre of a patient
 */
@AllArgsConstructor
@Getter
public enum Gender {
    M("Male"),
    F("Female");
    String label;
}
