package com.example.formservice.core;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "form_name")
@Data
public class FormEntity implements Serializable {

    private static final long serialVersionUID = -87208572351574652L;

    @Id
    @Column(unique = true)
    private String formId;
    private String name;
    private String description;
}
