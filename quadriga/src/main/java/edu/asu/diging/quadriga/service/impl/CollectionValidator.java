package edu.asu.diging.quadriga.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.asu.diging.quadriga.web.forms.CollectionForm;


@Service
public class CollectionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CollectionForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CollectionForm collectionForm = (CollectionForm) target;
        if (collectionForm.getName() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "collection.name.empty");
        }
    }
}