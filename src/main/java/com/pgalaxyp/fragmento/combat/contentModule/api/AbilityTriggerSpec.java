package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import java.util.Objects;

public record AbilityTriggerSpec(DamageSpec damage, TargetingSpec targeting) {
    public AbilityTriggerSpec {
        Objects.requireNonNull(damage);
        Objects.requireNonNull(targeting);
    }
}