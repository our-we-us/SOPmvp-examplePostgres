package com.example.formservice.command.rest;

import com.example.formservice.command.CreateFormCommand;
import com.example.formservice.command.DeleteFormCommand;
import com.example.formservice.command.UpdateFormCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/form")
public class FormCommandController {

    private final CommandGateway commandGateway;

    @Autowired
    public FormCommandController(CommandGateway commandGateway) {
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
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }

    @PatchMapping(value = "/{formId}")
    public String updateForm(@PathVariable(value = "formId") String formId,@RequestBody UpdateFormRestModel model) {
//        System.out.println(model);
        UpdateFormCommand command = UpdateFormCommand.builder()
                .formId(formId)
                .name(model.getName())
                .description(model.getDescription())
                .build();

        String result;
        try {
            result = commandGateway.sendAndWait(command);
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }

    @DeleteMapping(value = "/{formId}")
    public String deleteForm(@PathVariable(value = "formId") String formId) {
        System.out.println("hi");
        DeleteFormCommand command = DeleteFormCommand.builder()
                .formId(formId)
                .build();

        String result;
        try {
            result = commandGateway.sendAndWait(command);
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }
}
