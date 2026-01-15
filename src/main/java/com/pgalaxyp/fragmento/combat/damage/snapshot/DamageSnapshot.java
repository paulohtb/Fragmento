package com.pgalaxyp.fragmento.combat.damage.snapshot;

public record DamageSnapshot(ResistanceProfile targetResistances) {

    public DamageSnapshot {
        if (targetResistances == null) {
            throw new IllegalArgumentException();
        }
    }
}