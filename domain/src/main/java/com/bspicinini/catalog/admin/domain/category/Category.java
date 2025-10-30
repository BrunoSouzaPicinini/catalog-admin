package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.AggregateRoot;
import com.bspicinini.catalog.admin.domain.validation.ValidationHandler;

import java.time.LocalDateTime;

public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Category(final CategoryID id, final String name, final String description,
            final boolean isActive, final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = isActive ? null : this.createdAt;
    }

    public static Category newCategory(final String name, final String description,
            final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = LocalDateTime.now();
        return new Category(id, name, description, isActive, now, now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = LocalDateTime.now();
        }
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Category activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null;
        return this;
    }

    public Category update(final String name, final String description, final boolean isActive) {
        this.name = name;
        this.description = description;
        this.updatedAt = LocalDateTime.now();

        if (this.isActive != isActive) {
            if (isActive) {
                activate();
            } else {
                deactivate();
            }
        }
        return this;
    }
}
