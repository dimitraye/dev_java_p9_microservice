package com.example.manageAssesment.controller;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;
import com.example.manageAssesment.service.IAssesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class AssessController {

    @Autowired
    IAssesService assesService;
    private static String baseUrl = "http://localhost";
    private static String portNote = ":8082";
    private static String endpointNote = "/note";
    private static String endpointPatHistory = "/patHistory";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";

    private static String urlEndpoint;




    @RequestMapping("assess/{patId}")
    public String generateReport(@PathVariable Integer patId) {
        String uriPatient = baseUrl + portPatient + endpointPatient + "/" + patId;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uriPatient);
        Patient patient = restTemplate.getForObject(uriPatient, Patient.class);

        String uriNote = baseUrl + portNote + endpointPatHistory + "?patId=" + patId;

        log.info("Calling endpoint get notes : " + uriNote);
        Note[] notes = restTemplate.getForObject(uriNote, Note[].class);

        String risk = assesService.evaluateRisk(patient, List.of(notes));
        String report = assesService.generateReport(patient, risk);
        return report;
    }


}
