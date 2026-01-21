package com.pgalaxyp.fragmento.combat.ability.api;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.Objects;

public record AbilitySnapshot(AbilityId abilityId, ActorId actorId, long startFrame, long endFrameExclusive) {
    public AbilitySnapshot {
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(actorId);
        if (startFrame < 0 || endFrameExclusive <= startFrame) {
            throw new IllegalArgumentException();
        }
    }

    public boolean activeAt(long frame) {
        return frame >= startFrame && frame < endFrameExclusive;
    }
}