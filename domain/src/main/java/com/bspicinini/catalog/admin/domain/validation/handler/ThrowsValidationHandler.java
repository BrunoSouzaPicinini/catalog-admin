package com.bspicinini.catalog.admin.domain.validation.handler;

import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import com.bspicinini.catalog.admin.domain.validation.ValidationHandler;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(ValidationError error) {
        throw DomainException.with(List.of(error));
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            validation.validate();
        } catch (DomainException ex) {
            throw ex;
        } catch (Exception ex) {
            throw DomainException.with(List.of(new ValidationError(ex.getMessage())));
        }
        return this;
    }

    @Override
    public List<ValidationError> getErrors() {
        return List.of();
    }

}
