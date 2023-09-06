package com.example.manageUi.controller;

import com.example.manageUi.model.Note;
import com.example.manageUi.service.ConfDockerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

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
     * Return the list page and the list of note with it.
     * @param model
     * @return list page
     */
   /* @RequestMapping("/notes/{patId}")
    public String home(Model model, @PathVariable Integer patId)
    {
        String uri = baseUrl + portNote + endpointPatHistory + "?patId=" + patId;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get notes : " + uri);
        Note[] notes = restTemplate.getForObject(uri, Note[].class);

        model.addAttribute("notes", notes);
        return "note/list";
    }*/

    /**
     * Send the user to the Add page.
     * @return the Add page.
     */
    /*@GetMapping("/note/add/{patId}")
    public String add(Model model, @PathVariable Integer patId) {
        //Retourne l'endpoint note/add qui affiche la page add
        Note note = new Note();
        note.setPatId(patId);
        model.addAttribute("create", true);
        model.addAttribute("note", note);

        return "note/form";
    }*/

    /**
     * Validate the data of the formular and add the new note into the DB.
     * @param
     * @param
     * @param model
     * @return the list page.
     */
    /*@PostMapping("/note/validate")
    public String validate(@Valid Note note, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "note/form";
        }
        String uri = baseUrl + portNote + endpointPatHistory + "/add";

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint post note : " + uri);
        restTemplate.postForObject(uri, note, Note.class);

        return "redirect:/notes/" + note.getPatId();
    }*/


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

    /**
     *  Send the user to the update page.
     * @param id
     * @param model
     * @return the update page
     */
    /*@GetMapping("/note/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        String uri = baseUrl + portNote + endpointNote + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint get note : " + uri);
        Note note = restTemplate.getForObject(uri, Note.class);
        model.addAttribute("note", note);
        model.addAttribute("create", false);

        return "note/form";
    }*/

    /**
     * Check the required fields and save the update
     * @param id
     * @param note
     * @param result
     * @param model
     * @return the list page.
     */
    /*@PostMapping("/note/update/{id}")
    public String updateNote(@PathVariable("id") Integer id, @Valid Note note,
                              BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Note and return Note list
        if (result.hasErrors()) {
            model.addAttribute("create", false);

            //Retourne l'endpoint note/update qui affiche la page update
            return "note/form";
        }

        String uri = baseUrl + portNote + endpointNote + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint put note : " + uri);
        restTemplate.put(uri, note, Note.class);

        //Retourne l'endpoint note/list qui affiche la page list
        return "redirect:/notes/" + note.getPatId();
    }*/

    /**
     * Delete the note.
     * @param id
     * @return the list page.
     */
    /*@GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id") Integer id, @RequestParam Integer patId) {
        String uri = baseUrl + portNote + endpointNote + "/" + id;

        RestTemplate restTemplate = new RestTemplate();

        log.info("Calling endpoint delete note : " + uri);
        restTemplate.delete(uri);

        //Retourne l'endpoint note/list qui affiche la page list
        return "redirect:/notes/" + patId;
    }*/
}
