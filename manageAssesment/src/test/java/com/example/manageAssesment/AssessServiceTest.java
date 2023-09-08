package com.example.manageAssesment;

import com.example.manageAssesment.model.Gender;
import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;
import com.example.manageAssesment.service.AssessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssessServiceTest {

    @InjectMocks
    AssessServiceImpl assessService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldEvaluateRisk() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        note3.setContent("Rechute");
        patient.setDob(Date.valueOf("1999-12-25"));


        List<Note> notes = List.of(note1, note2, note3);

        String expectedRisk = assessService.IN_DANGER;

        String actualRisk = assessService.evaluateRisk(patient, notes);

        assertEquals(expectedRisk, actualRisk);
        System.out.println("Expected Risk : " + expectedRisk);
        System.out.println("Actual Risk : " + actualRisk);

    }

    @Test
    public void shouldReturnNbTermsInList() {
        //1 - Data creation
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");

        List<Note> notes = List.of(note1, note2, note3);

        //2 - Data processing
        int expectedNbTerms = 2;
        int actualNbTerms = assessService.nbTermsInNotes(notes);


        //3 - Test
        assertEquals(expectedNbTerms, actualNbTerms);
        System.out.println("Expected Terms : " + expectedNbTerms);
        System.out.println("Actual Terms : " + actualNbTerms);
    }

    @Test
    public void shouldGenerateReport_NONE() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        patient.setDob(Date.valueOf("1980-12-25"));


        List<Note> notes = List.of(note1, note2);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.NONE;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
    @Test
    public void shouldGenerateReport_BORDERLINE() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        patient.setDob(Date.valueOf("1980-12-25"));


        List<Note> notes = List.of(note1, note2);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.BORDELINE;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
    @Test
    public void shouldGenerateReport_IN_DANGER1_2() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        note3.setContent("Rechute");
        patient.setDob(Date.valueOf("1999-12-25"));


        List<Note> notes = List.of(note1, note2, note3);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.IN_DANGER;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }

    @Test
    public void shouldGenerateReport_IN_DANGER3() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        Note note4 = DataTest.getNoteTest2();
        Note note5 = DataTest.getNoteTest2();
        Note note6 = DataTest.getNoteTest2();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        note3.setContent("Rechute");
        note4.setContent("Vertige");
        note5.setContent("Microalbumine");
        note6.setContent("Taille");

        patient.setDob(Date.valueOf("1980-12-25"));


        List<Note> notes = List.of(note1, note2, note3, note4, note5, note6);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.IN_DANGER;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
    @Test
    public void shouldGenerateReport_EARLY_ONSET1_2() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        Note note4 = DataTest.getNoteTest2();
        Note note5 = DataTest.getNoteTest2();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        note3.setContent("Rechute");
        note4.setContent("Vertige");
        note5.setContent("Microalbumine");
        patient.setDob(Date.valueOf("1999-12-25"));


        List<Note> notes = List.of(note1, note2, note3, note4, note5);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.EARLY_ONSET;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }

    @Test
    public void shouldGenerateReport_EARLY_ONSET3() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        Note note4 = DataTest.getNoteTest2();
        Note note5 = DataTest.getNoteTest2();
        Note note6 = DataTest.getNoteTest2();
        Note note7 = DataTest.getNoteTest2();
        Note note8 = DataTest.getNoteTest2();

        patient.setSex(Gender.F);
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        note3.setContent("Rechute");
        note4.setContent("Vertige");
        note5.setContent("Taille");
        note6.setContent("Poids");
        note7.setContent("Fumeur");
        note8.setContent("Anormal");

        patient.setDob(Date.valueOf("1980-12-25"));


        List<Note> notes = List.of(note1, note2, note3, note4, note5, note6, note7, note8);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.EARLY_ONSET;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
    @Test
    public void shouldGenerateReport_UNKNOWN() {
        //1 - Data creation
        Patient patient = DataTest.getPatientTest1();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");
        patient.setDob(Date.valueOf("1980-12-25"));


        List<Note> notes = List.of(note1, note2);

        //2 - Data processing
        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.BORDELINE;

        String actualReport = assessService.generateReport(patient, risk);

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
}
