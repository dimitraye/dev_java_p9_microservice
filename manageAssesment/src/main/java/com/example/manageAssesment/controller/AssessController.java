package com.example.manageAssesment.controller;

import com.example.manageAssesment.model.Note;
import com.example.manageAssesment.model.Patient;
import com.example.manageAssesment.service.ConfDockerService;
import com.example.manageAssesment.service.IAssesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Manage the requests linked to an Assessment
 */
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


    AssessController(ConfDockerService confDockerService) {
        if (confDockerService.isDocker()) {
            baseUrl = "http://host.docker.internal";
            log.info("base_url in notecontroller " + baseUrl);
        }
    }


    /**
     * Generate a report based on the patient's notes and informations
     * @param patId
     * @return a report
     */
    @RequestMapping("assess/{patId}")
    public String generateReport(@PathVariable Integer patId) {
        //uri to access a patient
        String uriPatient = baseUrl + portPatient + endpointPatient + "/" + patId;

        //
        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uriPatient);
        //
        Patient patient = restTemplate.getForObject(uriPatient, Patient.class);

        //uri to access the notes of a patient
        String uriNote = baseUrl + portNote + endpointPatHistory + "?patId=" + patId;

        log.info("Calling endpoint get notes : " + uriNote);
        //list of all the notes of a patient
        Note[] notes = restTemplate.getForObject(uriNote, Note[].class);

        //evaluate the risk of diabetes based of the notes of a patient
        String risk = assesService.evaluateRisk(patient, List.of(notes));
        //generate a report
        String report = assesService.generateReport(patient, risk);
            return report;
    }


}
