package com.pgalaxyp.fragmento.combat.damageModule.port;

public record DamageSnapshot(ResistanceProfile targetResistances) {

    public DamageSnapshot {
        if (targetResistances == null) {
            throw new IllegalArgumentException();
        }
    }
}