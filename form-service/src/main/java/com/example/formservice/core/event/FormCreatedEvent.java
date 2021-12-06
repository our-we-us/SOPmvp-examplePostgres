package com.example.formservice.core.event;

import lombok.Data;

@Data
public class FormCreatedEvent {
    private String formId;
    private String name;
    private String description;
}
