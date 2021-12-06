package com.example.formservice.core.data;

import com.example.formservice.core.FormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormRepository extends JpaRepository<FormEntity, String> {

    FormEntity findByFormId(String formId);

    FormEntity findByFormIdOrName(String formId, String name);
}
