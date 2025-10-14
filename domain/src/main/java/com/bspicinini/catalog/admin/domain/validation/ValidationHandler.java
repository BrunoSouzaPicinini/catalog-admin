package com.bspicinini.catalog.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final ValidationError error);

    ValidationHandler append(final ValidationHandler handler);

    ValidationHandler validate(final Validation validation);

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<ValidationError> getErrors();

    public interface Validation {
        void validate();
    }
}
