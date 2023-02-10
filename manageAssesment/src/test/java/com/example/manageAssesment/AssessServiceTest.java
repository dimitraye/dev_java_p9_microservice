package com.example.manageAssesment;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;
import com.example.manageAssesment.service.AssessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AssessServiceTest {

    @InjectMocks
    AssessServiceImpl assessService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    //1
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

        //2 - Data processing

        //3 - Test
        assertEquals(expectedRisk, actualRisk);
        System.out.println("Expected Risk : " + expectedRisk);
        System.out.println("Actual Risk : " + actualRisk);

    }

    //2
    @Test
    public void shouldReturnNbTermsInList() {
        //1 - Data creation
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        note1.setContent("Anticorps");
        note2.setContent("Réaction");

        List<Note> notes = List.of(note1, note2, note3);

        int expectedNbTerms = 2;
        int actualNbTerms = assessService.nbTermsInNotes(notes);

        //2 - Data processing

        //3 - Test
        assertEquals(expectedNbTerms, actualNbTerms);
        System.out.println("Expected Terms : " + expectedNbTerms);
        System.out.println("Actual Terms : " + actualNbTerms);

    }

    //3
    @Test
    public void shouldGenerateReport() {
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

        String risk = assessService.evaluateRisk(patient, notes);

        String expectedReport = "Patient: " + patient.getGiven() + " " + patient.getFamily() + " (age " + patient.getAge() + ")" +
                " diabetes assessment is: " + assessService.IN_DANGER;

        String actualReport = assessService.generateReport(patient, risk);
        //2 - Data processing

        //3 - Test
        assertEquals(expectedReport, actualReport);
        System.out.println("Expected Report : " + expectedReport);
        System.out.println("Actual Report : " + actualReport);

    }
}
