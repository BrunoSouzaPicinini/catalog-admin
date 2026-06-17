package com.bspicinini.catalog.admin.application.category.retrive.get;

import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.category.CategoryID;
import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway =
                Objects.requireNonNull(categoryGateway, "'categoryGateway' should not be null");
    }

    @Override
    public CategoryOutput execute(final String anIn) {
        final var categoryId = CategoryID.from(anIn);
        return categoryGateway.findById(categoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(categoryId));
    }

    private Supplier<DomainException> notFound(final CategoryID categoryId) {
        return () -> DomainException.with(new ValidationError(
                "Category with ID %s was not found".formatted(categoryId.getValue())));
    }
}
