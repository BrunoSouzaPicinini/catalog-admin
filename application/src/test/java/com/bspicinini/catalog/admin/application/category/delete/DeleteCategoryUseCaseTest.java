package com.bspicinini.catalog.admin.application.category.delete;

import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
        useCase = new DefaultDeleteCategoryUseCase(categoryGateway);
    }


    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        var categoryId = CategoryID.from("123");
        final var expectedId = categoryId.getValue();

        Mockito.doNothing().when(categoryGateway).deleteById(categoryId);

        useCase.execute(expectedId);

        Mockito.verify(categoryGateway).deleteById(Mockito.argThat(aId -> Objects.equals(expectedId, aId.getValue())));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = "invalid-id";

        Mockito.doNothing().when(categoryGateway).deleteById(Mockito.any());

        useCase.execute(expectedId);

        Mockito.verify(categoryGateway).deleteById(Mockito.argThat(aId -> Objects.equals(expectedId, aId.getValue())));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldPropagate() {
        final var expectedId = "123";

        Mockito.doThrow(new RuntimeException("Gateway error")).when(categoryGateway).deleteById(Mockito.any());

        Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(expectedId));
        
        Mockito.verify(categoryGateway).deleteById(Mockito.argThat(aId -> Objects.equals(expectedId, aId.getValue())));
        
    }
}
