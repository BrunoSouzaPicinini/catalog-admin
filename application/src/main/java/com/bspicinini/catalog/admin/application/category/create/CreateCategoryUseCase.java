package com.bspicinini.catalog.admin.application.category.create;

import com.bspicinini.catalog.admin.application.UseCase;
import com.bspicinini.catalog.admin.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends
        UseCase<CreateCategoryCommand, Either<NotificationValidationHandler, CreateCategoryOutput>> {
}
