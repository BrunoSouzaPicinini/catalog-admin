package com.bspicinini.catalog.admin.application.category.retrive.get;

import com.bspicinini.catalog.admin.domain.category.Category;
import com.bspicinini.catalog.admin.domain.category.CategoryGateway;
import com.bspicinini.catalog.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnACategory() {
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.getId();
        Mockito.when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category));

        final var actualCategory = useCase.execute(expectedId.getValue());    

        assertThat(actualCategory).usingRecursiveComparison().isEqualTo(CategoryOutput.from(category));
        Mockito.verify(categoryGateway).findById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = "invalid-id";

        Mockito.when(categoryGateway.findById(Mockito.any())).thenReturn(Optional.empty());

        var actualException =Assertions.assertThrows(DomainException.class, () -> useCase.execute(expectedId));

        Assertions.assertEquals("Category with ID %s was not found".formatted(expectedId), actualException.getErrors().get(0).message());   
        Mockito.verify(categoryGateway).findById(Mockito.argThat(id -> id.getValue().equals(expectedId)));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldPropagate() {
        final var expectedId = "123";

        Mockito.when(categoryGateway.findById(Mockito.any()))
                .thenThrow(new RuntimeException("Gateway error"));

        Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(expectedId));

        Mockito.verify(categoryGateway).findById(Mockito.argThat(id -> id.getValue().equals(expectedId)));
    }

}
