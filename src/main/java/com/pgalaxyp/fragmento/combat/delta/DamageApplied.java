package com.pgalaxyp.fragmento.combat.delta;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;

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