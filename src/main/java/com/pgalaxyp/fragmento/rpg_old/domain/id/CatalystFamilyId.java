package com.pgalaxyp.fragmento.rpg_old.domain.id;

public record CatalystFamilyId(
        String value
) {
    public CatalystFamilyId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CatalystFamilyId vazio");
        }
    }
}