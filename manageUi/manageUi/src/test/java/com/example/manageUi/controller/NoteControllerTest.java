package com.example.manageUi.controller;

import com.example.manageUi.DataTest;
import com.example.manageUi.model.Note;
import com.example.manageUi.model.Patient;
import com.example.manageUi.service.ConfDockerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ConfDockerService.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /*@Autowired
    ConfDockerService confDockerService;*/

    //8 tests

    //1
    @Test
    public void shouldRedirectToHomeNotes() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);
        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(get("/notes/{patId}", String.valueOf(noteFromDB.getPatId())))
                .andExpect(status().isOk());

    }

    //2
    @Test
    public void shouldReturnAddForm() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(get("/note/add/{patId}", String.valueOf(patientFromDB.getId())))
                .andExpect(status().isOk());
    }

    //3
    @Test
    public void shouldCreatNoteWhenCallingValidate() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);


        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(post("/note/validate")
                .param("content", "Surpoid paracétamol")
                .param("patId", String.valueOf(patientFromDB.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/notes/" + patientFromDB.getId()));
    }

    //4
    @Test
    public void shouldReturnShow() throws Exception {
        // 1 - Creation data
        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);
        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(get("/note/{id}", String.valueOf(noteFromDB.getId())))
                .andExpect(status().isOk());


    }

    //5
    @Test
    public void shouldReturnUpdateForm() throws Exception {
        // 1 - Creation data
        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);
        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(get("/note/update/{id}", String.valueOf(noteFromDB.getId())))
                .andExpect(status().isOk());
    }

    //6
    @Test
    public void shouldUpdateNoteAfterCheckUpdateForm() throws Exception {
        // 1 - Creation data
        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);
        //2 - Data processing

        //3 - Test
        this.mockMvc
                .perform(post("/note/update/{id}", String.valueOf(noteFromDB.getId()))
                .param("patId", String.valueOf(noteFromDB.getPatId()))
                .param("content", "assurément large"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/notes/" + noteFromDB.getPatId()));
    }

    //7
    @Test
    public void shouldReturnDeleteNote() throws Exception {
        // 1 - Creation data

        String uriNote = "http://localhost:8082/patHistory/add";

        RestTemplate restTemplateNote = new RestTemplate();

        Note note = DataTest.getNoteTest1();

        //log.info("Calling endpoint post patient : " + uri);
        Note noteFromDB = restTemplateNote.postForObject(uriNote, note, Note.class);
        //2 - Data processing

        //3 - Test


        this.mockMvc
                .perform(get("/note/delete/{id}", String.valueOf(noteFromDB.getPatId()))
                .param("patId", String.valueOf(noteFromDB.getPatId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/notes/" + noteFromDB.getPatId()));
    }

    //8

}
