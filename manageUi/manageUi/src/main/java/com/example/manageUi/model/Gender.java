package com.example.manageUi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model that represent the gender of a patient
 */
@AllArgsConstructor
@Getter
public enum Gender {
    M("Male"),
    F("Female");
    String label;
}
