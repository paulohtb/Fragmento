package com.pgalaxyp.fragmento.rpg.core.event.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboEnded(
        ActorId actorId,
        ActionId actionId
) implements StateDelta {
    public ComboEnded {
        if (actorId == null || actionId == null) {
            throw new IllegalArgumentException();
        }
    }
}