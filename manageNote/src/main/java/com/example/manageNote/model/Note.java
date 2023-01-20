package com.example.manageNote.model;


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
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Id Patient is mandatory")
    private Integer patId;

    @NotBlank(message = "Content is mandatory")
    @Lob
    private String content;

    @Column(nullable = false, updatable = false)
    private Date creationDate;

}
