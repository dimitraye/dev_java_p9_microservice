package com.example.managePatient.repository;

import com.example.managePatient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    public List<Patient> findByGivenIgnoreCaseAndFamilyIgnoreCase(String given, String family);
}
