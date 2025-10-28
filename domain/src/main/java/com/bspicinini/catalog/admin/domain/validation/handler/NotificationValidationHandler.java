package com.bspicinini.catalog.admin.domain.validation.handler;

import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import com.bspicinini.catalog.admin.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.List;

public class NotificationValidationHandler implements ValidationHandler {

    private final List<ValidationError> errors;

    private NotificationValidationHandler(List<ValidationError> errors) {
        this.errors = errors;
    }

    public static NotificationValidationHandler create() {
    return new NotificationValidationHandler(new ArrayList<>());
    }

    public static NotificationValidationHandler create(ValidationError error) {
    List<ValidationError> list = new ArrayList<>();
    list.add(error);
    return new NotificationValidationHandler(list);
    }

    public static NotificationValidationHandler create(final Throwable t) {
       return create(new ValidationError(t.getMessage()));
    }

    @Override
    public NotificationValidationHandler append(final ValidationError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public NotificationValidationHandler append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public NotificationValidationHandler validate(final Validation validation) {
        try {
            validation.validate();
        } catch (final DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (final Throwable e) {
            this.errors.add(new ValidationError(e.getMessage()));
        }

        
        return this;
    }

    @Override
    public List<ValidationError> getErrors() {
        return this.errors;
    }

}
