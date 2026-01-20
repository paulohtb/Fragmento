package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.EffectId;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingSpec;
import java.util.Objects;

public record AbilityDef(
        AbilityId id,
        int durationFrames,
        int cooldownFrames,
        EffectId startEffect,
        TargetingSpec targeting
) {
    public AbilityDef {
        Objects.requireNonNull(id);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        if (durationFrames <= 0) throw new IllegalArgumentException();
        if (cooldownFrames < 0) throw new IllegalArgumentException();
    }

    public long endFrameExclusive(long startFrame) { return Math.addExact(startFrame, durationFrames); }
    public long lockEndInclusive(long startFrame) { return endFrameExclusive(startFrame); }
    public long cooldownEndExclusive(long startFrame) { return cooldownFrames <= 0 ? -1L : Math.addExact(startFrame, cooldownFrames); }
}