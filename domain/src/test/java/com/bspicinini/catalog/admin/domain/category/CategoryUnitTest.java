package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import com.bspicinini.catalog.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class CategoryUnitTest {

    @Test
    void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Category 1";
        final var expectedDescription = "Category description";
        final var expectedIsActive = true;
        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "Category description";
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(validationHandler);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "";
        final var expectedDescription = "Category description";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(validationHandler);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("invalidNameLengthProvider")
    void givenAnInvalidNameLength_whenCallNewCategoryAndValidate_thenShouldReceiveError(
            String expectedName, String scenarioName) {
        final var expectedDescription = "Category description";
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters long";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var validationHandler = new ThrowsValidationHandler();

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(validationHandler);
        });

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    private static Stream<Arguments> invalidNameLengthProvider() {
        return Stream.of(Arguments.of("ab", "less than 3"),
                Arguments.of("a".repeat(256), "greater than 255"));
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveOk() {
        final String expectedName = "Movies";
        final var expectedDescription = "";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var validationHandler = new ThrowsValidationHandler();

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(validationHandler);
        });

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveOk() {
        final String expectedName = "Movies";
        final var expectedDescription = "";
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallDeactivate_thenCategoryShouldBeInactive() {
        final String expectedName = "Movies";
        final var expectedDescription = "A Category description";

        final var category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.deactivate();

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidInactiveCategory_whenCallDeactivate_thenCategoryShouldBeActiveted() {
        final String expectedName = "Movies";
        final var expectedDescription = "A Category description";

        final var category = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        final var actualCategory = category.activate();

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final String expectedName = "Movies and Series";
        final var expectedDescription = "A Category description";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Movie", null, expectedIsActive);

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            category.validate(new ThrowsValidationHandler());
        });

        final var actualCategory =
                category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final String expectedName = "Movies and Series";
        final var expectedDescription = "A Category description";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Movie", null, true);

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            category.validate(new ThrowsValidationHandler());
        });

        final var actualCategory = category.update(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdateToActive_thenReturnCategoryUpdated() {
        final String expectedName = "Movies and Series";
        final var expectedDescription = "A Category description";

        final var category = Category.newCategory("Movie", null, false);

        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            category.validate(new ThrowsValidationHandler());
        });

        final var actualCategory = category.update(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

     @Test
    void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "A Category description";        

        final var category = Category.newCategory("Movie", null, true);

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            category.validate(new ThrowsValidationHandler());
        });

        final var actualCategory = category.update(expectedName, expectedDescription, true);

        Assertions.assertEquals(actualCategory.getId(), category.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

}
