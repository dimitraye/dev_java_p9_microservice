package com.example.manageAssesment;

import com.example.manageAssesment.controller.AssessController;
import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;
import com.example.manageAssesment.service.AssessServiceImpl;
import com.example.manageAssesment.service.ConfDockerService;
import com.example.manageAssesment.service.IAssesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessController.class)
public class AssessControllerTest {

    @MockBean
    IAssesService assessService;

    @MockBean
    ConfDockerService dockerService;

    @Autowired
    MockMvc mockMvc;


    //1
    @Test
    public void shouldGenerateReport() throws Exception {
        //1 - Prepare Data
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();
        note2.setPatId(note1.getPatId());
        note3.setPatId(note1.getPatId());
        List<Note> expectedNotes = List.of(note1, note2, note3);
        Patient patient = DataTest.getPatientTest1();
        String expectedRisk = AssessServiceImpl.NONE;
        String expectedReport = "Patient: Shadows The Hedgehog (age"
                + patient.getAge() +
                ") diabetes assessment is: " + AssessServiceImpl.NONE;

        //2 - Data Processing (mock service calls)
        when(assessService.evaluateRisk(any(Patient.class), anyList()))
                .thenReturn(expectedRisk);
        when(assessService.generateReport(any(Patient.class), anyString()))
                .thenReturn(expectedReport);

        //3 - Test
        MvcResult result = mockMvc.perform(get("/assess/{patId}", String.valueOf(note1.getPatId())))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(expectedReport, content);
    }
}
