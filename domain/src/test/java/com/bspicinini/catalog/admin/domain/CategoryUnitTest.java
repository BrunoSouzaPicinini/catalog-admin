package com.bspicinini.catalog.admin.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryUnitTest {


    @Test
    public void testNewCategory() {
        final Category category = new Category();
        Assertions.assertNotNull(category);
    }

}
