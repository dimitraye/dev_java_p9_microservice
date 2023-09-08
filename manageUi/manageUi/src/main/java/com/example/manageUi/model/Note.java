package com.example.manageUi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Model that represents the note
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {


    private Integer id;

    @NotNull(message = "Id Patient is mandatory")
    private Integer patId;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private Date creationDate;

}
