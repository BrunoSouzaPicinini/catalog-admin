package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.pagination.Pagination;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    Category update(Category category);

    void deleteById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
