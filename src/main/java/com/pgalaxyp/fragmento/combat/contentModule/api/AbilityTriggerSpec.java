package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import java.util.Objects;

public record AbilityTriggerSpec(EffectId effectId, TargetingSpec targeting) {
    public AbilityTriggerSpec {
        Objects.requireNonNull(effectId);
        Objects.requireNonNull(targeting);
    }
}