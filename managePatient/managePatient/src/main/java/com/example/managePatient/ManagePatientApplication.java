package com.example.managePatient;

import com.example.managePatient.model.Patient;
import com.example.managePatient.repository.PatientRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootApplication
public class ManagePatientApplication {

	@Autowired
	public PatientRepository patientRepository;
	public ManagePatientApplication(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
	public static void main(String[] args) {
		SpringApplication.run(ManagePatientApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {

		return args -> {
			List<Patient> patients = null;
			log.info("reading json");
			ObjectMapper mapper = new ObjectMapper();
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			mapper.setDateFormat(formater);
			TypeReference<List<Patient>> typeReferencePatient = new TypeReference<>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/data/patients.json");
			try {
				log.info("saving the json in the db");
				patients = mapper.readValue(inputStream,typeReferencePatient);
				patientRepository.saveAll(patients);
				log.info("Patients Saved!");
			} catch (IOException e){
				log.error("Unable to save patients: " + e.getMessage());
			}
		};
	}

}
