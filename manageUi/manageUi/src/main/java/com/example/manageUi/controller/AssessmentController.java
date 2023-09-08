package com.example.manageUi.controller;

import com.example.manageUi.service.ConfDockerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

/**
 * Manage the requests linked to an assassment
 */
@Slf4j
@Controller
public class AssessmentController {

    private static String baseUrl = "http://localhost";
    private static String portNote = ":8082";
    private static String endpointNote = "/note";
    private static String endpointPatHistory = "/patHistory";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";

    private static String portAssessment = ":8080";
    private static String endpointAssessment = "/assess";

    private static String urlEndpoint;

    ObjectMapper mapper;


     AssessmentController(ConfDockerService confDockerService) {
         mapper = new ObjectMapper();

         if (confDockerService.isDocker()) {
             baseUrl = "http://host.docker.internal";
             log.info("base_url in asessmentcontroller " + baseUrl);
         }

         urlEndpoint = baseUrl + portAssessment ;
     }


    /**
     *
     * @param patId
     * @param model
     * @return
     */
    @GetMapping("/assess/{patId}")
    public String show(@PathVariable Integer patId, Model model) {
        String uri = baseUrl + portAssessment + endpointAssessment + "/" + patId;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get assessment : " + uri);
        String report = restTemplate.getForObject(uri, String.class);
        model.addAttribute("report", report);

        return "assesment/show";
    }

}
