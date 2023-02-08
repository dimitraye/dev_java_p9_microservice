package com.example.manageNote;

import com.example.manageNote.controller.NoteController;
import com.example.manageNote.model.Note;
import com.example.manageNote.repository.NoteRepository;
import com.example.manageNote.service.ConfDockerService;
import com.example.manageNote.service.INoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @MockBean
    INoteService noteService;

    @MockBean
    ConfDockerService confDockerService;

    @MockBean
    NoteRepository noteRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    //6 tests


    //1
    @Test
    public void shouldCreateNote() throws Exception {

        //Date creation
        Note noteTest1 = DataTest.getNoteTest1();
        String noteJson = "{" +
                "\"patId\":\"1\"," +
                "\"content\":\"En bonne santé\"" +
                "}";


        when(noteService.paramTojson(anyString())).thenReturn(noteJson);
        when(noteService.getValidationErrors(noteTest1)).thenReturn(null);
        when(noteService.checkPatient(anyString())).thenReturn(true);
        when(noteService.save(noteTest1)).thenReturn(noteTest1);

        mockMvc.perform(post("/patHistory/add").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteTest1)))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @Test
    public void shouldGetAllNotes() throws Exception {
        //data creation
        List<Note> list = new ArrayList<>();
        Note note1 = DataTest.getNoteTest1();
        Note note2 = DataTest.getNoteTest2();
        Note note3 = DataTest.getNoteTest3();

        //data processing
        list.add(note1);
        list.add(note2);
        list.add(note3);

        when(noteService.findAll()).thenReturn(list);

        //test

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()))
                .andDo(print());
    }

    @Test
    public void shouldFindById() throws Exception {
        Note noteTest1 = DataTest.getNoteTest1();

        when(noteService.findNoteById(noteTest1.getId())).thenReturn(Optional.of(noteTest1));

        mockMvc.perform(get("/note/{id}", String.valueOf(noteTest1.getId())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldFindByPatId() throws Exception {
        List<Note> list = new ArrayList<>();
        Note noteTest1 = DataTest.getNoteTest1();
        Note noteTest2 = DataTest.getNoteTest2();
        Note noteTest3 = DataTest.getNoteTest3();
        noteTest2.setPatId(noteTest1.getPatId());
        noteTest3.setPatId(noteTest1.getPatId());

        list.add(noteTest1);
        list.add(noteTest2);
        list.add(noteTest3);


        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("patId", String.valueOf(noteTest1.getPatId()));

        when(noteService.findByPatId(anyInt())).thenReturn(list);

        mockMvc.perform(get("/patHistory").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()))
                .andDo(print());
    }



    @Test
    public void ShouldUpdateNote() throws  Exception{
        Note noteTest1 = DataTest.getNoteTest1();

        Note updatedNote = DataTest.getNoteTest1();
        updatedNote.setContent("De nouveau obèse");



        when(noteService.findNoteById(noteTest1.getId())).thenReturn(Optional.of(noteTest1));
        when(noteService.getValidationErrors(noteTest1)).thenReturn(null);
        when(noteService.save(any(Note.class))).thenReturn(updatedNote);

        mockMvc.perform(put("/note/{id}", noteTest1.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedNote.getId()))
                .andExpect(jsonPath("$.content").value(updatedNote.getContent()))
                .andDo(print());

    }


    @Test
    public void shouldDeleteNote() throws Exception {
        Integer id = 95;

        doNothing().when(noteService).delete(id);
        mockMvc.perform(delete("/note/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }



}
