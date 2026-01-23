package com.pgalaxyp.fragmento.combat.damageModule.api;

public record DamageResult(int finalHearts, DamageType type, DamageElement element) {

    public DamageResult {
        if (finalHearts <= 0) {
            throw new IllegalArgumentException();
        }
        if (type == null || element == null) {
            throw new IllegalArgumentException();
        }
    }
}