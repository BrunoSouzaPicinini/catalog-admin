package com.bspicinini.catalog.admin.application.category.update;

import com.bspicinini.catalog.admin.domain.category.Category;
import com.bspicinini.catalog.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }

}
