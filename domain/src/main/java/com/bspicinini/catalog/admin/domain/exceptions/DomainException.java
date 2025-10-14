package com.bspicinini.catalog.admin.domain.exceptions;

import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import java.util.List;

public class DomainException extends RuntimeException {
    private final List<ValidationError> errors;

    private DomainException(final List<ValidationError> errors) {
        super("", null, true, false);
        this.errors = errors;
    }

    public static DomainException with(final List<ValidationError> errors) {
        return new DomainException(errors);
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

}
