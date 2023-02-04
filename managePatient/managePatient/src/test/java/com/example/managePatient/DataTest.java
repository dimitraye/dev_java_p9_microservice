package com.example.managePatient;

import com.example.managePatient.model.Gender;
import com.example.managePatient.model.Patient;

import java.sql.Date;

public class DataTest {
    public static Patient getPatientTest1() {
        Patient patientTest1 = new Patient();
        patientTest1.setGiven("Sahdow");
        patientTest1.setFamily("The Hedgehog");
        patientTest1.setDob(Date.valueOf("1999-12-25"));
        patientTest1.setSex(Gender.M);
        patientTest1.setAddress("1 Brookside St");
        patientTest1.setPhone("111-222-000");
        return patientTest1;
    }

    public static Patient getPatientTest2() {
        Patient patientTest2 = new Patient();
        patientTest2.setGiven("Knucklke");
        patientTest2.setFamily("The Porcupine");
        patientTest2.setDob(Date.valueOf("2000-12-25"));
        patientTest2.setSex(Gender.M);
        patientTest2.setAddress("2 Ancient Ruins");
        patientTest2.setPhone("222-000-111");
        return patientTest2;
    }

    public static Patient getPatientTest3() {
        Patient patientTest3 = new Patient();
        patientTest3.setGiven("Tails");
        patientTest3.setFamily("The Fox");
        patientTest3.setDob(Date.valueOf("2001-12-25"));
        patientTest3.setSex(Gender.M);
        patientTest3.setAddress("3 Sillicon Tech");
        patientTest3.setPhone("000-111-222");
        return patientTest3;
    }
}
