package com.example.formservice.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class DeleteFormCommand {
    @TargetAggregateIdentifier
    private final String formId;

}
