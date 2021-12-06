package com.example.formservice.query;

import com.example.formservice.core.FormEntity;
import com.example.formservice.core.data.FormRepository;
import com.example.formservice.core.event.FormCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FormEventsHandler {

    private final FormRepository formRepository;

    public FormEventsHandler(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @EventHandler
    public void on(FormCreatedEvent event) {
        FormEntity formEntity = new FormEntity();
        BeanUtils.copyProperties(event, formEntity);
        formRepository.save(formEntity);
    }
}
