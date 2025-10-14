package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import com.bspicinini.catalog.admin.domain.validation.ValidationHandler;
import com.bspicinini.catalog.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if (name == null) {
            this.validationHandler().append(new ValidationError("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new ValidationError("'name' should not be empty"));
            return;
        }
        if (name.trim().length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            this.validationHandler().append(new ValidationError("'name' should be between 3 and 255 characters long"));
            return;
        }
       
    }

}
