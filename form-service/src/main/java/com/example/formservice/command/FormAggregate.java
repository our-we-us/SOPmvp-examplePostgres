package com.example.formservice.command;


import com.example.formservice.core.FormEntity;
import com.example.formservice.core.event.FormCreatedEvent;
import com.example.formservice.core.event.FormDeleteEvent;
import com.example.formservice.core.event.FormUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class FormAggregate {

    @AggregateIdentifier
    private String formID;
    private String name;
    private String description;

    public FormAggregate() {
    }

    @CommandHandler
    public FormAggregate(CreateFormCommand createFormCommand) {
        if(createFormCommand.getName() == null || createFormCommand.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        FormCreatedEvent formCreatedEvent = new FormCreatedEvent();
        BeanUtils.copyProperties(createFormCommand, formCreatedEvent);
        AggregateLifecycle.apply(formCreatedEvent);
    }

    @EventSourcingHandler
    public void on(FormCreatedEvent formCreatedEvent) {
        this.formID = formCreatedEvent.getFormId();
        this.name = formCreatedEvent.getName();
        this.description = formCreatedEvent.getDescription();
    }

    @CommandHandler
    public void handle(UpdateFormCommand updateFormCommand) {
        FormUpdatedEvent formUpdatedEvent = new FormUpdatedEvent();
        BeanUtils.copyProperties(updateFormCommand, formUpdatedEvent);
        AggregateLifecycle.apply(formUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(FormUpdatedEvent formUpdatedEvent) {
        this.name = formUpdatedEvent.getName();
        this.description = formUpdatedEvent.getDescription();
    }

    @CommandHandler
    public void handle(DeleteFormCommand deleteFormCommand) {
        FormDeleteEvent formDeleteEvent = new FormDeleteEvent();
        BeanUtils.copyProperties(deleteFormCommand, formDeleteEvent);
        AggregateLifecycle.apply(formDeleteEvent);
    }

    @EventSourcingHandler
    public void on(FormDeleteEvent formDeleteEvent) {

    }
}
