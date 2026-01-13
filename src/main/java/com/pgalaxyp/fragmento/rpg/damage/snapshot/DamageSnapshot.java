package com.pgalaxyp.fragmento.rpg.damage.snapshot;

public record DamageSnapshot(ResistanceProfile targetResistances) {

    public DamageSnapshot {
        if (targetResistances == null) {
            throw new IllegalArgumentException();
        }
    }
}