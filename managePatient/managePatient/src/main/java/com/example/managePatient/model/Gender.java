package com.example.managePatient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model that represent the patient's gender
 */
@AllArgsConstructor
@Getter
public enum Gender {
    M("Male"),
    F("Female");

    String label;
}
