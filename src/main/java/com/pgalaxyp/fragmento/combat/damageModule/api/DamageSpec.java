package com.pgalaxyp.fragmento.combat.damageModule.api;

public record DamageSpec(int baseHearts, DamageType type, DamageElement element) {

    public DamageSpec {
        if (baseHearts <= 0) throw new IllegalArgumentException();
        if (type == null || element == null) throw new IllegalArgumentException();
    }
}