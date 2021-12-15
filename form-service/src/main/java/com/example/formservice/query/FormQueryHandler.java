package com.example.formservice.query;

import com.example.formservice.core.FormEntity;
import com.example.formservice.core.data.FormRepository;
import com.example.formservice.query.rest.FormRestModel;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FormQueryHandler {
    private final FormRepository formRepository;

    public FormQueryHandler(FormRepository formRepository){
        this.formRepository = formRepository;
    }

    @QueryHandler
    List<FormRestModel> findForms(FindFormQuery query){
        List<FormRestModel> formRest = new ArrayList<>();
        List<FormEntity> storedForms = formRepository.findAll();
        for(FormEntity formEntity: storedForms){
            FormRestModel formRestModel = new FormRestModel();
            BeanUtils.copyProperties(formEntity,formRestModel);
            formRest.add(formRestModel);
        }

        return  formRest;
    }
}
