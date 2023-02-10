package com.example.manageAssesment;

import com.example.manageAssesment.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {

    @Test
    public void shouldCalculateAge() {
        Date birthdate = DataTest.getPatientTest1().getDob();

        int expectedAge = 42;
        int actualAge = DateUtils.calculateAge(birthdate);

        assertEquals(expectedAge, actualAge);
        System.out.println("Expected Age : " + expectedAge);
        System.out.println("Actual Age : " + actualAge);
    }
}
