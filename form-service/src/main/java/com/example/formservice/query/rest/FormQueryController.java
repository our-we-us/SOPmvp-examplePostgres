package com.example.formservice.query.rest;

import com.example.formservice.query.FindFormQuery;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/form")
public class FormQueryController {
    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<FormRestModel> getForm(){
        FindFormQuery findFormQuery = new FindFormQuery();
        List<FormRestModel> forms = queryGateway.query(findFormQuery, ResponseTypes.multipleInstancesOf(FormRestModel.class)).join();
        return  forms;
    }
}
