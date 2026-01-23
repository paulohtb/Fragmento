package com.pgalaxyp.fragmento.combat.abilityModule.api;

import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import java.util.Objects;

public record AbilityDefinition(AbilityId id, int durationFrames, int cooldownFrames, EffectId startEffect, TargetingSpec targeting) {
    public AbilityDefinition {
        Objects.requireNonNull(id);
        Objects.requireNonNull(startEffect);
        Objects.requireNonNull(targeting);
        if (durationFrames <= 0) throw new IllegalArgumentException();
        if (cooldownFrames < 0) throw new IllegalArgumentException();
    }

    public long endFrameExclusive(long startFrame) {
        return Math.addExact(startFrame, durationFrames);
    }

    public long cooldownEndExclusive(long startFrame) {
        if (cooldownFrames == 0) return -1L;
        return Math.addExact(startFrame, cooldownFrames);
    }
}