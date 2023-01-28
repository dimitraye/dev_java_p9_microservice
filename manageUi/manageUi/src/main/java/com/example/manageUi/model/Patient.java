package com.example.manageUi.model;

import com.example.manageUi.utils.DateUtils;
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
public class Patient {
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

    public Integer getAge(){
        if (dob != null) {
            return DateUtils.calculateAge(dob);
        }
        return null;
    }

}
