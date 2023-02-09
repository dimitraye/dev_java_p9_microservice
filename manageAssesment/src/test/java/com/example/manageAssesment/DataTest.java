package com.example.manageAssesment;

import com.example.manageAssesment.model.Gender;
import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;

import java.sql.Date;

public class DataTest {
    public static Patient getPatientTest1() {
        Patient patientTest1 = new Patient();
        patientTest1.setId(1);
        patientTest1.setGiven("Sahdow");
        patientTest1.setFamily("The Hedgehog");
        patientTest1.setDob(Date.valueOf("1980-12-25"));
        patientTest1.setSex(Gender.M);
        patientTest1.setAddress("1 Brookside St");
        patientTest1.setPhone("111-222-000");
        return patientTest1;
    }



    public static Note getNoteTest1() {
        Note noteTest1 = new Note();
        noteTest1.setId(1);
        noteTest1.setPatId(1);
        noteTest1.setContent("Obèse");
        noteTest1.setCreationDate(Date.valueOf("2005-12-25"));
        return noteTest1;
    }

    public static Note getNoteTest2() {
        Note noteTest2 = new Note();
        noteTest2.setId(2);
        noteTest2.setPatId(2);
        noteTest2.setContent("Obésité morbide");
        noteTest2.setCreationDate(Date.valueOf("2009-12-25"));
        return noteTest2;
    }

    public static Note getNoteTest3() {
        Note noteTest3 = new Note();
        noteTest3.setId(3);
        noteTest3.setPatId(3);
        noteTest3.setContent("Bonne santé");
        noteTest3.setCreationDate(Date.valueOf("2016-12-25"));
        return noteTest3;
    }
}
