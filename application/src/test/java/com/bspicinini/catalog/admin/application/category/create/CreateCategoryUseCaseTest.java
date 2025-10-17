package com.bspicinini.catalog.admin.application.category.create;

import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

        @InjectMocks
        private DefaultCreateCategoryUseCase useCase;

        @Mock
        private CategoryGateway categoryGateway;

        @Test
        void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;

                final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(categoryGateway.create(Mockito.any()))
                                .thenAnswer(AdditionalAnswers.returnsFirstArg());

                final var actualOutput = useCase.execute(aCommand);

                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(categoryGateway).create(Mockito.argThat(aCategory -> Objects
                                .equals(expectedName, aCategory.getName())
                                && Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals(expectedIsActive, aCategory.isActive())
                                && Objects.nonNull(aCategory.getId())
                                && Objects.nonNull(aCategory.getCreatedAt())
                                && Objects.nonNull(aCategory.getUpdatedAt())
                                && Objects.isNull(aCategory.getDeletedAt())));
        }

        @Test
        void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
                final String expectedName = null;
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;
                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
                                expectedIsActive);

                final var actualException = Assertions.assertThrows(DomainException.class,
                                () -> useCase.execute(aCommand));

                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage,
                                actualException.getErrors().get(0).message());

                Mockito.verify(categoryGateway, Mockito.never()).create(Mockito.any());
        }

        @Test
        void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = false;

                final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(categoryGateway.create(Mockito.any()))
                                .thenAnswer(AdditionalAnswers.returnsFirstArg());

                final var actualOutput = useCase.execute(aCommand);

                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(categoryGateway).create(Mockito.argThat(aCategory -> Objects
                                .equals(expectedName, aCategory.getName())
                                && Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals(expectedIsActive, aCategory.isActive())
                                && Objects.nonNull(aCategory.getId())
                                && Objects.nonNull(aCategory.getCreatedAt())
                                && Objects.nonNull(aCategory.getUpdatedAt())
                                && Objects.nonNull(aCategory.getDeletedAt())));
        }

        @Test
        void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;
                final var expectedErrorMessage = "Gateway error";

                final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(categoryGateway.create(Mockito.any()))
                                .thenThrow(new IllegalStateException(expectedErrorMessage));

                final var actualException = Assertions.assertThrows(IllegalStateException.class,
                                () -> useCase.execute(aCommand));

                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

                Mockito.verify(categoryGateway).create(Mockito.argThat(aCategory -> Objects
                                .equals(expectedName, aCategory.getName())
                                && Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals(expectedIsActive, aCategory.isActive())
                                && Objects.nonNull(aCategory.getId())
                                && Objects.nonNull(aCategory.getCreatedAt())
                                && Objects.nonNull(aCategory.getUpdatedAt())
                                && Objects.isNull(aCategory.getDeletedAt())));

        }
}
