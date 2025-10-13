package com.bspicinini.catalog.admin.application;

import com.bspicinini.catalog.admin.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {

    @Test
    public void testExecute() {
        UseCase useCase = new UseCase();
        Category category = useCase.execute();

        Assertions.assertNotNull(category);
    }

}
