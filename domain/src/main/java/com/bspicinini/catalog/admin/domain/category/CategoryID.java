package com.bspicinini.catalog.admin.domain.category;

import com.bspicinini.catalog.admin.domain.Identifier;
import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {

    private final String value;

    private CategoryID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CategoryID unique() {
        return CategoryID.from(java.util.UUID.randomUUID());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId) {
        return new CategoryID(anId.toString().toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CategoryID other = (CategoryID) obj;
        return getValue().equals(other.getValue());
    }


}
