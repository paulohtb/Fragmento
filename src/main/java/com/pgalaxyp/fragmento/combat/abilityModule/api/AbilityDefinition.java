package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.*;

public record AbilityDefinition(AbilityId id, int durationFrames, int cooldownFrames) {
    public AbilityDefinition {
        Objects.requireNonNull(id);
        if (durationFrames <= 0) throw new IllegalArgumentException();
        if (cooldownFrames < 0) throw new IllegalArgumentException();
    }

    public long endFrameExclusive(long startFrame) {
        return Math.addExact(startFrame, durationFrames);
    }

    public OptionalLong cooldownEndExclusive(long startFrame) {
        return cooldownFrames == 0 ? OptionalLong.empty() : OptionalLong.of(Math.addExact(startFrame, cooldownFrames));
    }
}