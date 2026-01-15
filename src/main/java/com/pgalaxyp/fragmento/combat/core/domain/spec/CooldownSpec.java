package com.pgalaxyp.fragmento.combat.core.domain.spec;

public record CooldownSpec(
        int frames
) {
    public CooldownSpec {
        if (frames <= 0) {
            throw new IllegalArgumentException();
        }
    }
}