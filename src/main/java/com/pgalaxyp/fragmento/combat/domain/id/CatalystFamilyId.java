package com.pgalaxyp.fragmento.combat.domain.id;

public record CatalystFamilyId(
        String value
) {
    public CatalystFamilyId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CatalystFamilyId vazio");
        }
    }
}