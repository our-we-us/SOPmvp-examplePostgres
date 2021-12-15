package com.example.formservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class UpdateFormCommand {
    @TargetAggregateIdentifier
    private final String formId;

    private final String name;
    private final String description;
}
