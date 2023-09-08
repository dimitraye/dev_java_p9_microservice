package com.example.manageUi.controller;

import com.example.manageUi.model.Patient;
import com.example.manageUi.service.ConfDockerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

/**
 * Manage the requests linked to a Patient
 */
@Slf4j
@Controller
public class PatientController {

    private static String baseUrl = "http://localhost";
    private static String portPatient = ":8081";
    private static String endpointPatient = "/patient";
    private static String urlEndpoint;

    ObjectMapper mapper;


     PatientController(ConfDockerService confDockerService) {
         mapper = new ObjectMapper();

         if (confDockerService.isDocker()) {
             baseUrl = "http://host.docker.internal";
             log.info("base_url in patientcontroller " + baseUrl);
         }

         urlEndpoint = baseUrl + portPatient + endpointPatient;
     }
    /**
     * Return the list page and the list of patient with it.
     * @param model
     * @return list page
     */
    @RequestMapping("/patients")
    public String home(Model model)
    {
        String uri = urlEndpoint + "s";

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patients : " + uri);
        Patient[] patients = restTemplate.getForObject(uri, Patient[].class);

        model.addAttribute("patients", patients);
        return "patient/list";
    }

    /**
     * Send the user to the Add page.
     * @return the Add page.
     */
    @GetMapping("/patient/add")
    public String add(Model model, Patient patient) {
        //Retourne l'endpoint patient/add qui affiche la page add
        model.addAttribute("create", true);

        return "patient/form";
    }

    /**
     * Validate the data of the formular and add the new patient into the DB.
     * @param patient
     * @param result
     * @param model
     * @return the list page.
     */
    @PostMapping("/patient/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "patient/form";
        }
        String uri = urlEndpoint + "/add";

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint post patient : " + uri);
        restTemplate.postForObject(uri, patient, Patient.class);

        return "redirect:/patients";
    }


    /**
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/patient/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        String uri = urlEndpoint + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uri);
        Patient patient = restTemplate.getForObject(uri, Patient.class);
        model.addAttribute("patient", patient);

        return "patient/show";
    }

    /**
     *  Send the user to the update page.
     * @param id
     * @param model
     * @return the update page
     */
    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        String uri = urlEndpoint + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get patient : " + uri);
        Patient patient = restTemplate.getForObject(uri, Patient.class);
        model.addAttribute("patient", patient);
        model.addAttribute("create", false);

        return "patient/form";
    }

    /**
     * Check the required fields and save the update
     * @param id
     * @param patient
     * @param result
     * @param model
     * @return the list page.
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable Integer id, @Valid Patient patient,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);

            return "patient/form";
        }

        String uri = urlEndpoint + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint put patient : " + uri);
        restTemplate.put(uri, patient, Patient.class);
        return "redirect:/patients";
    }

    /**
     * Delete the patient.
     * @param id
     * @return the list page.
     */
    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Integer id) {
        String uri = urlEndpoint + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint delete patient : " + uri);
        restTemplate.delete(uri);

        return "redirect:/patients";
    }
}
