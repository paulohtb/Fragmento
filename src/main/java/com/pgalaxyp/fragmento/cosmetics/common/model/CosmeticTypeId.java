package com.pgalaxyp.fragmento.cosmetics.common.model;

import java.util.Objects;

public record CosmeticTypeId(String value) {

    public CosmeticTypeId {
        Objects.requireNonNull(value, "value");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value");
        }
    }

    public static CosmeticTypeId of(String value) {
        return new CosmeticTypeId(value);
    }
}