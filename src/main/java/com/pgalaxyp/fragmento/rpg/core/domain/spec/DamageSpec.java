package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record DamageSpec(
        int hearts
) {
    public DamageSpec {
        if (hearts <= 0) {
            throw new IllegalArgumentException();
        }
    }
}