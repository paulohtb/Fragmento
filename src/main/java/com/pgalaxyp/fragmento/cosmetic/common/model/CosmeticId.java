package com.pgalaxyp.fragmento.cosmetic.common.model;

import java.util.Objects;

public record CosmeticId(String value) {

    public CosmeticId {
        Objects.requireNonNull(value, "value");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("value");
        }
    }

    public static CosmeticId of(String value) {
        return new CosmeticId(value);
    }
}