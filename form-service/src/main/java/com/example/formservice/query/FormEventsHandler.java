package com.example.formservice.query;

import com.example.formservice.core.FormEntity;
import com.example.formservice.core.data.FormRepository;
import com.example.formservice.core.event.FormCreatedEvent;
import com.example.formservice.core.event.FormDeleteEvent;
import com.example.formservice.core.event.FormUpdatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

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

    @EventHandler
    public void on(FormUpdatedEvent event) {
        Optional<FormEntity> optionalFormEntity = this.formRepository.findById(event.getFormId());
        if (optionalFormEntity.isPresent()) {
            FormEntity formEntity = optionalFormEntity.get();
            formEntity.setName(event.getName());
            formEntity.setDescription(event.getDescription());
            formRepository.save(formEntity);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @EventHandler
    public void on(FormDeleteEvent event) {
        Optional<FormEntity> optionalFormEntity = this.formRepository.findById(event.getFormId());
        if (optionalFormEntity.isPresent()) {
            FormEntity formEntity = optionalFormEntity.get();
            formRepository.delete(formEntity);
        } else {
            throw new EntityNotFoundException();
        }
    }
}


