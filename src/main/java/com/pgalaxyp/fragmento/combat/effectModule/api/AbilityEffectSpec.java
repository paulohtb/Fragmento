package com.pgalaxyp.fragmento.combat.effectModule.api;

import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import java.util.Objects;

public record AbilityEffectSpec(EffectId effectId, TargetingSpec targeting) {
    public AbilityEffectSpec {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(targeting);
    }
}