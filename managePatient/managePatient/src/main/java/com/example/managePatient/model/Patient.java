package com.example.managePatient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "firstname is mandatory")
    private String given;
    @NotBlank(message = "lastname is mandatory")
    private String family;
    @NotNull(message = "Date of Birth is mandatory")
    private Date dob;
    @NotNull(message = "Gender is mandatory")
    @Enumerated(EnumType.STRING)
    private Gender sex;
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "Phone is mandatory")
    private String phone;

}
