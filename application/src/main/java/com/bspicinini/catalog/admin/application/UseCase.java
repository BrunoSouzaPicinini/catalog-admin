package com.bspicinini.catalog.admin.application;

import com.bspicinini.catalog.admin.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("name", "description", true);
    }

}
