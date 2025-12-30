package com.pgalaxyp.fragmento.combat.domain.infusion;

public record InfusionId(String value) {
    public InfusionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InfusionId vazio");
        }
    }
}