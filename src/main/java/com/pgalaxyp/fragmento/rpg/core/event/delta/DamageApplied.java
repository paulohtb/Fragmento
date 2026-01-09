package com.pgalaxyp.fragmento.rpg.core.event.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record DamageApplied(
        ActorId targetActorId,
        int hearts
) implements StateDelta {
    public DamageApplied {
        if (targetActorId == null) {
            throw new IllegalArgumentException();
        }
        if (hearts <= 0) {
            throw new IllegalArgumentException();
        }
    }
}