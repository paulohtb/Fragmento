package com.pgalaxyp.fragmento.combat.state.snapshot;

public record WeaponSnapshot(
        int comboIndex
) {
    public WeaponSnapshot {
        if (comboIndex < 0) {
            comboIndex = 0;
        }
    }

    public static WeaponSnapshot idle() {
        return new WeaponSnapshot(0);
    }
}