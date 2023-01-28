package com.example.manageNote.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Note {
    @Transient
    public static final String SEQUENCE_NAME = "notes_sequence";

    @Id
    private Integer id;

    @NotNull(message = "Id Patient is mandatory")
    private Integer patId;

    @NotBlank(message = "Content is mandatory")
    @Lob
    private String content;

    @Column(nullable = false, updatable = false)
    private Date creationDate;

}