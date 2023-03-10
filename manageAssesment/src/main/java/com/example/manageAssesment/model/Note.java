package com.example.manageAssesment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * Model that represent the note
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {


    private Integer id;

    private Integer patId;

    private String content;

    private Date creationDate;

}
