package com.pgalaxyp.fragmento.combat.domain.id;

public record ActionId(
        String value
) {
    public ActionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ActionId must not be blank");
        }
    }
}