package com.example.formservice.rest;

import com.example.formservice.command.CreateFormCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/form")
public class FormController {

    private final CommandGateway commandGateway;

    @Autowired
    public FormController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createTest(@RequestBody CreateFormRestModel model) {
        CreateFormCommand command = CreateFormCommand.builder()
                .formId(UUID.randomUUID().toString())
                .name(model.getName())
                .description(model.getDescription())
                .build();

        String result;
        try {
            result = commandGateway.sendAndWait(command);
        }catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }

}
