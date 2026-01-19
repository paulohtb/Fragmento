package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.EffectId;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingSpec;
import java.util.*;

public record AbilityDef(
        AbilityId id,
        int durationFrames,
        EffectId startEffect,
        TargetingSpec targeting
) {
    public AbilityDef {
        Objects.requireNonNull(id);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        if (durationFrames <= 0) throw new IllegalArgumentException();
    }
}
