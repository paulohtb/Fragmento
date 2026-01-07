package com.pgalaxyp.fragmento.rpg.core.domain.action;

public record ActionId(String value) {
    public ActionId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("ActionId.value");
    }
}