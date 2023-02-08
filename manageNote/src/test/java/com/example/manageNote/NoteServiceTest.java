package com.example.manageNote;

import com.example.manageNote.model.Note;
import com.example.manageNote.repository.NoteRepository;
import com.example.manageNote.service.ConfDockerService;
import com.example.manageNote.service.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NoteServiceTest {



    @Mock
    NoteRepository noteRepository;

    @InjectMocks
    NoteServiceImpl noteService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    //6 methods

    @Test
    public void shouldSave() throws Exception{
        Note noteTest1 = DataTest.getNoteTest1();

        noteService.save(noteTest1);

        verify(noteRepository, times(1)).save(noteTest1);
    }

    @Test
    public void shouldFindAll() throws Exception{

        List<Note> list = new ArrayList<>();
        Note noteTest1 = DataTest.getNoteTest1();
        Note noteTest2 = DataTest.getNoteTest2();
        Note noteTest3 = DataTest.getNoteTest3();

        list.add(noteTest1);
        list.add(noteTest2);
        list.add(noteTest3);

        when(noteService.findAll()).thenReturn(list);

        List<Note> noteList = noteService.findAll();

        assertEquals(3, noteList.size());
        verify(noteRepository, times(1)).findAll();
        System.out.println("list : " + list);
        System.out.println("noteLis : " + noteList);
    }

    @Test
    public void shouldFindNoteById() throws Exception{
        Note noteTest1 = DataTest.getNoteTest1();

        noteTest1.setId(1);

        when(noteRepository.findById(1)).thenReturn(Optional.of(noteTest1));
        Note note = noteService.findNoteById(noteTest1.getId()).get();

        assertEquals(noteTest1.getId(), note.getId());
    }

    @Test
    public void shouldFindNoteByPatId() throws Exception{
        Note noteTest1 = DataTest.getNoteTest1();
        Note noteTest2 = DataTest.getNoteTest2();

        noteTest2.setPatId(noteTest1.getPatId());

        List<Note> list = new ArrayList<>();
        list.add(noteTest1);
        list.add(noteTest2);

        when(noteRepository.findByPatId(noteTest1.getId())).thenReturn(list);

        List<Note> noteList = noteRepository.findByPatId(noteTest1.getPatId());
        assertEquals(2, list.size());
        verify(noteRepository, times(1)).findByPatId(noteTest1.getId());
    }

    @Test
    public void shouldDelete() throws Exception{
        Note noteTest1 = DataTest.getNoteTest1();

        noteService.save(noteTest1);
        noteService.delete(noteTest1.getId());

        verify(noteRepository, times(1)).save(noteTest1);
        verify(noteRepository, times(1)).deleteById(noteTest1.getId());
    }


    @Test
    public void shouldTransformParamToJson() {
//1 - Creation data
        String noteParam = "content=obese";

        String expectedJson = "{" +
                "\"content\":\"obese\"\n" +
                "}";

        expectedJson = expectedJson.replaceAll("\n", "")
                .replaceAll("\r", "");


        String actualResultJson = noteService.paramTojson(noteParam);

        assertEquals(expectedJson.strip(), actualResultJson.strip());
    }

    @Test
    public void shouldGetValidationErrors () {
        Note note = DataTest.getNoteTest1();

        ResponseEntity responseEntity = noteService.getValidationErrors(note);

        assertNull(responseEntity);
    }

    @Test
    public void shouldReturnErrorWhenPatientNotValidGetValidationErrors () {
        Note note = DataTest.getNoteTest1();
        note.setContent("");

        ResponseEntity responseEntity = noteService.getValidationErrors(note);

        assertNotNull(responseEntity);
    }

}
