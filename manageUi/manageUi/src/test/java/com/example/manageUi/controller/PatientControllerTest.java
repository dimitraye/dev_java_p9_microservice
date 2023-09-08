package com.example.manageUi.controller;

import com.example.manageUi.DataTest;
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
@WebMvcTest(PatientController.class)
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRedirectToHome() throws Exception {
        this.mockMvc
                .perform(get("/patients"))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnAddForm() throws Exception {
        this.mockMvc
                .perform(get("/patient/add"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreatPatientWhenCallingValidate() throws Exception {
        this.mockMvc
                .perform(post("/patient/validate")
                //.param("id", "2500")
                .param("given", "Dayle")
                .param("family", "Xeyb")
                .param("dob", "1999-12-15")
                .param("sex", "M")
                .param("address", "11111 rrrrr eeeee")
                .param("phone", "00000001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/patients"));
    }

    @Test
    public void shouldReturnShow() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        //2 - Test
        this.mockMvc
                .perform(get("/patient/{id}", String.valueOf(patientFromDB.getId())))
                .andExpect(status().isOk());


    }

    @Test
    public void shouldReturnUpdateForm() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        //2 - Test
        this.mockMvc
                .perform(get("/patient/update/{id}", String.valueOf(patientFromDB.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatePatientAfterCheckUpdateForm() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        //2 - Test
        this.mockMvc
                .perform(post("/patient/update/{id}", String.valueOf(patientFromDB.getId()))
                        .param("given", "Dayle")
                        .param("family", "Xeyb")
                        .param("dob", "1999-12-15")
                        .param("sex", "M")
                        .param("address", "11111 rrrrr eeeee")
                        .param("phone", "00000001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/patients"));
    }

    @Test
    public void shouldReturnDelete() throws Exception {
        // 1 - Creation data
        String uri = "http://localhost:8081/patient/add";

        RestTemplate restTemplate = new RestTemplate();

        Patient patient = DataTest.getPatientTest1();

        Patient patientFromDB = restTemplate.postForObject(uri, patient, Patient.class);

        //2 - Test
        this.mockMvc
                .perform(get("/patient/delete/{id}", String.valueOf(patientFromDB.getId())))
                .andExpect(status().is3xxRedirection());

    }


}
