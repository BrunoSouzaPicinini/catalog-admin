package com.bspicinini.catalog.admin.domain.exceptions;

import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import java.util.List;

public class DomainException extends NoStacktraceException {
    private final List<ValidationError> errors;

    private DomainException(final String message, final List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final List<ValidationError> errors) {
        return new DomainException("", errors);
    }

    public static DomainException with(final ValidationError validationError) {
        return new DomainException(validationError.message(), List.of(validationError));
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

}
