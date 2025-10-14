package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import com.bspicinini.catalog.admin.domain.validation.ValidationHandler;
import com.bspicinini.catalog.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        if (this.category.getName() == null) {
            this.validationHandler().append(new ValidationError("'name' should not be null"));
            return;
        }
    }

}
