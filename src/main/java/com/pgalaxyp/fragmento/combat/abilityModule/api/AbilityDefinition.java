package com.pgalaxyp.fragmento.combat.abilityModule.api;

import java.util.Objects;

public record AbilityDefinition(AbilityId id, int durationFrames, int cooldownFrames) {
    public AbilityDefinition {
        Objects.requireNonNull(id);
        if (durationFrames <= 0) throw new IllegalArgumentException();
        if (cooldownFrames < 0) throw new IllegalArgumentException();
    }

    public long endFrameExclusive(long startFrame) {
        return Math.addExact(startFrame, durationFrames);
    }

    public long cooldownEndExclusive(long startFrame) {
        return cooldownFrames == 0 ? -1L : Math.addExact(startFrame, cooldownFrames);
    }
}