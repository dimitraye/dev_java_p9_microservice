package com.example.manageUi.controller;

import com.example.manageUi.DataTest;
import com.example.manageUi.model.Note;
import com.example.manageUi.model.Patient;
import com.example.manageUi.service.ConfDockerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ConfDockerService.class)
@Slf4j
@WebMvcTest(AssessmentController.class)
public class AssessmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnShow() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        log.info("Calling endpoint post patient : " + uri);
        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        note.setPatId(patientFromDB.getId());
        log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);

        //2 - Test
        this.mockMvc
                .perform(get("/assess/{patId}", String.valueOf(noteFromDB.getPatId())))
                .andExpect(status().isOk());
    }

}
