package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageElement;

import java.util.*;

public final class ResistanceProfile {

    private final EnumMap<DamageElement, Float> resistances;

    public ResistanceProfile(Map<DamageElement, Float> resistances) {
        this.resistances = new EnumMap<>(DamageElement.class);
        if (resistances != null) {
            for (var entry : resistances.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    throw new IllegalArgumentException();
                }
                this.resistances.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public float resistanceFor(DamageElement element) {
        return resistances.getOrDefault(element, 0.0f);
    }

    public static ResistanceProfile none() {
        return new ResistanceProfile(Map.of());
    }
}