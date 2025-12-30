package com.pgalaxyp.fragmento.combat.domain.action;

public record ActionId(String value) {
    public ActionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ActionId vazio");
        }
    }
}