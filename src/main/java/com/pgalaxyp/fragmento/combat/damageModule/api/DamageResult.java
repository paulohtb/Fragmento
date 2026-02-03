package com.pgalaxyp.fragmento.combat.damageModule.api;

public record DamageResult(int damageHearts, DamageType type, DamageElement element) {
    public DamageResult {
        if (damageHearts <= 0) throw new IllegalArgumentException();
        if (type == null || element == null) throw new IllegalArgumentException();
    }
}