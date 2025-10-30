package com.bspicinini.catalog.admin.application.category.update;

import com.bspicinini.catalog.admin.domain.category.Category;
import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.category.CategoryID;
import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import com.bspicinini.catalog.admin.domain.validation.ValidationError;
import com.bspicinini.catalog.admin.domain.validation.handler.NotificationValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

        private final CategoryGateway categoryGateway;

        public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
                this.categoryGateway = Objects.requireNonNull(categoryGateway);
        }

        @Override
        public Either<NotificationValidationHandler, UpdateCategoryOutput> execute(
                        final UpdateCategoryCommand aCommand) {
                final var anId = CategoryID.from(aCommand.id());
                final String aName = aCommand.name();
                final String aDescription = aCommand.description();
                final boolean isActive = aCommand.isActive();

                final var aCategory = this.categoryGateway.findById(anId)
                                .orElseThrow(notFound(anId));
                final var notification = NotificationValidationHandler.create();
                aCategory.update(aName, aDescription, isActive)
                                .validate(notification);

                return notification.hasErrors() ? API.Left(notification) : update(aCategory);
        }

        private Either<NotificationValidationHandler, UpdateCategoryOutput> update(final Category aCategory) {
                return API.Try(() -> this.categoryGateway.update(aCategory))
                                .toEither()
                                .bimap(NotificationValidationHandler::create, UpdateCategoryOutput::from);
        }

        private Supplier<DomainException> notFound(final CategoryID anId) {
                return () -> DomainException
                                .with(new ValidationError(
                                                "Category with ID %s was not found".formatted(anId.getValue())));
        }

}
