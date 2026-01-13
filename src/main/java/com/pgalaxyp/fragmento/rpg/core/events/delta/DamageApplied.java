package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record DamageApplied(
        ActorId targetActorId,
        int hearts
) implements StateDelta {
    public DamageApplied {
        if (targetActorId == null || hearts <= 0) {
            throw new IllegalArgumentException();
        }
    }
}