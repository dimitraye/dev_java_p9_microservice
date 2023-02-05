package com.example.managePatient;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import com.example.managePatient.service.PatientServiceImpl;
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
public class PatientServiceTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientServiceImpl patientService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSave() throws Exception{
        Patient patientTest1 = DataTest.getPatientTest1();

        patientService.save(patientTest1);

        verify(patientRepository, times(1)).save(patientTest1);
    }

    @Test
    public void shouldFindPatientById() throws Exception{
        Patient patientTest1 = DataTest.getPatientTest1();


        patientTest1.setId(1);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patientTest1));
        Patient patient = patientService.findPatientById(patientTest1.getId()).get();

        /* assertEquals("15 rue des champs", firestation.getAddress());
    assertEquals(5, firestation.getStation());*/
        assertEquals(patientTest1.getId(), patient.getId());

    }

    @Test
    public void shouldFindAll() throws Exception{
        List<Patient> list = new ArrayList<>();
        Patient patientTest1 = DataTest.getPatientTest1();
        Patient patientTest2 = DataTest.getPatientTest2();
        Patient patientTest3 = DataTest.getPatientTest3();

        list.add(patientTest1);
        list.add(patientTest2);
        list.add(patientTest3);

        when(patientService.findAll()).thenReturn(list);

        List<Patient> patientList = patientService.findAll();

        assertEquals(3, patientList.size());
        verify(patientRepository, times(1)).findAll();
        System.out.println("List : " + list);
        System.out.println("patientList : " + patientList);
    }

    @Test
    public void shouldDelete() throws Exception{
        Patient patientTest1 = new Patient();
        patientTest1 = DataTest.getPatientTest1();
        patientTest1.setId(12);

        patientService.save(patientTest1);
        patientService.delete(patientTest1.getId());

        verify(patientRepository, times(1)).save(patientTest1);
        verify(patientRepository, times(1)).deleteById(patientTest1.getId());
    }

    @Test
    public void shoulFindByGivenAndFamily() throws Exception{
// 1 - Data Creation :
        Patient patientTest1 = DataTest.getPatientTest1();
        Patient patientTest2 = DataTest.getPatientTest2();

        patientTest2.setGiven(patientTest1.getGiven());
        patientTest2.setFamily(patientTest1.getFamily());

        List<Patient> list = new ArrayList<>();
        list.add(patientTest1);
        list.add(patientTest2);

        when(patientRepository.findByGivenIgnoreCaseAndFamilyIgnoreCase(patientTest1.getGiven(), patientTest1.getFamily())).thenReturn(list);

        List<Patient> patientList = patientService.findByGivenAndFamily(patientTest1.getGiven(), patientTest1.getFamily());

        assertEquals(2, patientList.size());
        verify(patientRepository, times(1)).findByGivenIgnoreCaseAndFamilyIgnoreCase(patientTest1.getGiven(), patientTest1.getFamily());
    }


    @Test
    public void shouldTransformParamToJson() {
//1 - Creation data
        String patientParam = "given=Shadow" +
                "&family=The Hedgehog" +
                "&dob=1999-12-25" +
                "&sex=M" +
                "&address=1 Brookside St" +
                "&phone=111-222-000";

        String expectedJson = "{" +
                "\"given\":\"Shadow\",\n" +
                "\"family\":\"The Hedgehog\",\n" +
                "\"dob\":\"1999-12-25\",\n" +
                "\"sex\":\"M\",\n" +
                "\"address\":\"1 Brookside St\",\n" +
                "\"phone\":\"111-222-000\"\n" +
                "}";

        expectedJson = expectedJson.replaceAll("\n", "")
                .replaceAll("\r", "");


        String actualResultJson = patientService.paramTojson(patientParam);

        assertEquals(expectedJson.strip(), actualResultJson.strip());
    }

    @Test
    public void shouldGetValidationErrors () {
        Patient patient = DataTest.getPatientTest1();

        ResponseEntity responseEntity = patientService.getValidationErrors(patient);

        assertNull(responseEntity);
    }

    @Test
    public void shouldReturnErrorWhenPatientNotValidGetValidationErrors () {
        Patient patient = DataTest.getPatientTest1();
        patient.setAddress("");

        ResponseEntity responseEntity = patientService.getValidationErrors(patient);

        assertNotNull(responseEntity);
    }
}
