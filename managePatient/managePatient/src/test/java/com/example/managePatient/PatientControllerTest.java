package com.example.managePatient;

import com.example.managePatient.controller.PatientController;
import com.example.managePatient.model.Gender;
import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import com.example.managePatient.service.IPatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PatientController.class)
class PatientControllerTest {

	@MockBean
	IPatientService patientService;

	@MockBean
	PatientRepository patientRepository;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void shouldCreatePatient() throws Exception {

		Patient patientTest1 = DataTest.getPatientTest1();
		String patientJson = "{" +
				"\"given\":\"Shadow\",\n" +
				"\"family\":\"The Hedgehog\",\n" +
				"\"dob\":\"1999-12-25\",\n" +
				"\"sex\":\"M\",\n" +
				"\"address\":\"1 Brookside St\",\n" +
				"\"phone\":\"111-222-000\"\n" +
				"}";


		when(patientService.paramTojson(anyString())).thenReturn(patientJson);
		//when(patientService.getValidationErrors(patientTest1)).thenReturn(null);
		when(patientService.save(patientTest1)).thenReturn(patientTest1);

		mockMvc.perform(post("/patient/add").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(patientTest1)))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	public void shouldFindById() throws Exception {
		Patient patientTest1 = new Patient();
		patientTest1.setId(45);
		patientTest1.setGiven("Sahdow");
		patientTest1.setFamily("The Hedgehog");
		patientTest1.setDob(Date.valueOf("1999-02-15"));
		patientTest1.setSex(Gender.M);
		patientTest1.setAddress("1 Brookside St");
		patientTest1.setPhone("111-222-000");

		when(patientService.findPatientById(patientTest1.getId())).thenReturn(Optional.of(patientTest1));

		mockMvc.perform(get("/patient/{id}", String.valueOf(patientTest1.getId())))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void shouldFindPatientExist() throws Exception {
		Patient patientTest1 = new Patient();
		patientTest1.setId(45);
		patientTest1.setGiven("Sahdow");
		patientTest1.setFamily("The Hedgehog");
		patientTest1.setDob(Date.valueOf("1999-02-15"));
		patientTest1.setSex(Gender.M);
		patientTest1.setAddress("1 Brookside St");
		patientTest1.setPhone("111-222-000");

		when(patientService.findPatientById(patientTest1.getId()))
				.thenReturn(Optional.of(patientTest1));
		mockMvc.perform(get("/patient/exist/{id}", patientTest1.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void shouldGetAllPatients() throws Exception {
		//data creation
		List<Patient> list = new ArrayList<>();
		Patient patient1 = DataTest.getPatientTest1();
		Patient patient2 = DataTest.getPatientTest2();
		Patient patient3 = DataTest.getPatientTest3();

		//data processing
		list.add(patient1);
		list.add(patient2);
		list.add(patient3);

		when(patientService.findAll()).thenReturn(list);

		//test
		mockMvc.perform(get("/patients"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(list.size()))
				.andDo(print());
	}

	@Test
	public void shouldReturnAllPatientsByGivenAndFamily() throws Exception {
		//data creation
		List<Patient> list = new ArrayList<>();
		Patient patient1 = DataTest.getPatientTest1();
		Patient patient2 = DataTest.getPatientTest1();
		Patient patient3 = DataTest.getPatientTest2();

		String given = "Shadow";
		String family = "The Hedgehog";

		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("given", given);
		paramsMap.add("family", family);

		//data processing
		list.add(patient1);
		list.add(patient2);
		list.add(patient3);

		when(patientService.findByGivenAndFamily(given, family)).thenReturn(list);
		mockMvc.perform(get("/patients/givenfamily").params(paramsMap))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(list.size()))
				.andDo(print());
	}

	@Test
	public void shouldGetPatientByGivenAndFamily() throws Exception {
		//data creation
		Patient patient1 = DataTest.getPatientTest1();

		String given = patient1.getGiven();
		String family = patient1.getFamily();

		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("given", given);
		paramsMap.add("family", family);

		when(patientService.findPatientByGivenAndFamily(given, family)).thenReturn(patient1);
		mockMvc.perform(get("/patient/givenfamily").params(paramsMap))
				.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void ShouldUpdatePatient() throws  Exception{
		Patient patientTest1 = new Patient();
		patientTest1.setId(45);
		patientTest1.setGiven("Sahdow");
		patientTest1.setFamily("The Hedgehog");
		patientTest1.setDob(Date.valueOf("1999-02-15"));
		patientTest1.setSex(Gender.M);
		patientTest1.setAddress("1 Brookside St");
		patientTest1.setPhone("111-222-000");

		Patient updatedPatient = new Patient();
		updatedPatient.setId(45);
		updatedPatient.setGiven("Sahdow");
		updatedPatient.setFamily("The Hedgehog");
		updatedPatient.setDob(Date.valueOf("1999-02-15"));
		updatedPatient.setSex(Gender.M);
		updatedPatient.setAddress("1 Brookside St");
		updatedPatient.setPhone("111-222-000");

		when(patientService.findPatientById(patientTest1.getId())).thenReturn(Optional.of(patientTest1));
		//when(patientService.getValidationErrors(patientTest1)).thenReturn(null);
		when(patientService.save(any(Patient.class))).thenReturn(updatedPatient);

		mockMvc.perform(put("/patient/{id}", patientTest1.getId()).contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedPatient)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedPatient.getId()))
				.andExpect(jsonPath("$.given").value(updatedPatient.getGiven()))
				.andExpect(jsonPath("$.family").value(updatedPatient.getFamily()))
				.andExpect(jsonPath("$.address").value(updatedPatient.getAddress()))
				.andExpect(jsonPath("$.phone").value(updatedPatient.getPhone()))
				.andDo(print());

	}

	@Test
	public void shouldDeletePatient() throws Exception {
		Integer id = 45 ;

		doNothing().when(patientService).delete(id);
		mockMvc.perform(delete("/patient/{id}", id))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
}
