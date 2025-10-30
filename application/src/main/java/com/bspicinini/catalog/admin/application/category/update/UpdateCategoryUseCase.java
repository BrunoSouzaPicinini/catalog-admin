package com.bspicinini.catalog.admin.application.category.update;

import com.bspicinini.catalog.admin.application.UseCase;
import com.bspicinini.catalog.admin.domain.validation.handler.NotificationValidationHandler;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<NotificationValidationHandler, UpdateCategoryOutput>> {

}
