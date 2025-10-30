package com.bspicinini.catalog.admin.application.category.update;

import com.bspicinini.catalog.admin.domain.category.Category;
import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.category.CategoryID;
import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

        @InjectMocks
        private DefaultUpdateCategoryUseCase useCase;

        @Mock
        private CategoryGateway categoryGateway;

        @Test
        void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;

                final var expectedId = aCategory.getId();

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription,
                                expectedIsActive);

                Category aCategoryClone = aCategory.clone();
                when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategoryClone));
                when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

                final var actualOutput = useCase.execute(aCommand).get();

                assertNotNull(actualOutput);
                assertNotNull(actualOutput.id());

                verify(categoryGateway).findById(eq(expectedId));
                verify(categoryGateway).update(argThat(
                                aUpdatedCategory -> Objects.equals(aUpdatedCategory.getId(), expectedId)
                                                && Objects.equals(aUpdatedCategory.getName(), expectedName)
                                                && Objects.equals(aUpdatedCategory.getDescription(),
                                                                expectedDescription)
                                                && Objects.equals(aUpdatedCategory.isActive(), expectedIsActive)
                                                && Objects.equals(aUpdatedCategory.getCreatedAt(),
                                                                aCategory.getCreatedAt())
                                                && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
        }

        @Test
        void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
                final var aCategory = Category.newCategory("Film", null, true);

                final String expectedName = null;
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;

                final var expectedId = aCategory.getId();

                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription,
                                expectedIsActive);

                when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

                final var notification = useCase.execute(aCommand).getLeft();

                assertEquals(expectedErrorCount, notification.getErrors().size());
                assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

                verify(categoryGateway).findById(eq(expectedId));
                verifyNoMoreInteractions(categoryGateway);
        }

        @Test
        void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactivatedCategoryId() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = false;

                final var expectedId = aCategory.getId();

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription,
                                expectedIsActive);

                when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
                when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

                final var actualOutput = useCase.execute(aCommand).get();

                assertNotNull(actualOutput);
                assertNotNull(actualOutput.id());

                verify(categoryGateway).findById(eq(expectedId));
                verify(categoryGateway).update(argThat(
                                aUpdatedCategory -> Objects.equals(aUpdatedCategory.getId(), expectedId)
                                                && Objects.equals(aUpdatedCategory.getName(), expectedName)
                                                && Objects.equals(aUpdatedCategory.getDescription(),
                                                                expectedDescription)
                                                && Objects.equals(aUpdatedCategory.isActive(), expectedIsActive)
                                                && Objects.equals(aUpdatedCategory.getCreatedAt(),
                                                                aCategory.getCreatedAt())
                                                && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())));
        }

        @Test
        void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = true;

                final var expectedId = aCategory.getId();

                final var expectedErrorMessage = "Gateway error";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription,
                                expectedIsActive);

                when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
                when(categoryGateway.update(any()))
                                .thenThrow(new IllegalStateException(expectedErrorMessage));

                final var notification = useCase.execute(aCommand).getLeft();

                assertEquals(1, notification.getErrors().size());
                assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

                verify(categoryGateway).findById(eq(expectedId));
                verify(categoryGateway).update(argThat(
                                aUpdatedCategory -> Objects.equals(aUpdatedCategory.getId(), expectedId)
                                                && Objects.equals(aUpdatedCategory.getName(), expectedName)
                                                && Objects.equals(aUpdatedCategory.getDescription(),
                                                                expectedDescription)
                                                && Objects.equals(aUpdatedCategory.isActive(), expectedIsActive)
                                                && Objects.equals(aUpdatedCategory.getCreatedAt(),
                                                                aCategory.getCreatedAt())
                                                && aCategory.getCreatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
        }

        @Test
        void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() {        
                final var expectedName = "Movies";
                final var expectedDescription = "Most watched category";
                final var expectedIsActive = false;

                final var expectedId = CategoryID.from("1");
                final String expectedErrorMessage = "Category with ID 1 was not found";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription,
                                expectedIsActive);

                when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.empty());                

                final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));                

                
                assertEquals(
                                expectedErrorMessage,
                                actualException.getErrors().get(0).message()
                );
                assertEquals(expectedErrorCount, actualException.getErrors().size());

                verify(categoryGateway).findById(eq(expectedId));
                verify(categoryGateway, never()).update(any());
        }
}
