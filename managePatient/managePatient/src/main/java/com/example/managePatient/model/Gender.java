package com.example.managePatient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    M("Male"),
    F("Female");

    String label;
}
