package com.example.manageAssesment.model;

import com.example.manageAssesment.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * Model that represents the patient
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Integer id;
    private String given;
    private String family;
    private Date dob;

    private Gender sex;
    private String address;
    private String phone;

    public Integer getAge(){
        if (dob != null) {
            return DateUtils.calculateAge(dob);
        }
        return null;
    }

    public boolean isOlderthan30() {
        Integer age = getAge();
        if (age != null & age > 30) {
            return true;
        }
        return false;
    }

    public boolean isYougerthan30() {
        Integer age = getAge();
        if (age != null & age < 30) {
            return true;
        }
        return false;
    }

    public boolean isMale() {
        return Gender.M.equals(sex);
    }

    public boolean isFemale() {
        return Gender.F.equals(sex);
    }

}
