package com.example.managePatient;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import com.example.managePatient.service.IPatientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootApplication
public class ManagePatientApplication {

	@Autowired
	PatientRepository patientRepository;
	public static void main(String[] args) {
		SpringApplication.run(ManagePatientApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {

		return args -> {
			List<Patient> patients = null;
			// read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			mapper.setDateFormat(formater);
			TypeReference<List<Patient>> typeReferencePatient = new TypeReference<>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/data/patients.json");
			try {
				patients = mapper.readValue(inputStream,typeReferencePatient);
				patientRepository.saveAll(patients);
				System.out.println("Patients Saved!");
			} catch (IOException e){
				System.out.println("Unable to save patients: " + e.getMessage());
			}
		};
	}

}
