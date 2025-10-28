package com.bspicinini.catalog.admin.application.category.create;

import com.bspicinini.catalog.admin.domain.category.Category;
import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.validation.handler.NotificationValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<NotificationValidationHandler, CreateCategoryOutput> execute(
            CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var notification = NotificationValidationHandler.create();
        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        aCategory.validate(notification);

        return notification.hasErrors() ? API.Left(notification) : create(aCategory);
    }

    private Either<NotificationValidationHandler, CreateCategoryOutput> create(Category aCategory) {
        return API.Try(() -> this.categoryGateway.create(aCategory)).toEither().bimap(
                NotificationValidationHandler::create,
                createdCategory -> CreateCategoryOutput.from(createdCategory));
    }

}
