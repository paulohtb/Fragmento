package com.pgalaxyp.fragmento.combat.damage.domain;

import com.pgalaxyp.fragmento.combat.targeting.api.*;

public record DamageSpec(int baseHearts, DamageType type, DamageElement element, TargetingSpec targeting) {

    public DamageSpec {
        if (baseHearts <= 0) throw new IllegalArgumentException();
        if (type == null || element == null || targeting == null) throw new IllegalArgumentException();
    }

    public DamageSpec(int baseHearts, DamageType type, DamageElement element) {
        this(baseHearts, type, element, new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 8, TargetingFallback.SELF));
    }
}