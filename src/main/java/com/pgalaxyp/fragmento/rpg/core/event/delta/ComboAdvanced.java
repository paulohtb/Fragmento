package com.pgalaxyp.fragmento.rpg.core.event.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboAdvanced(
        ActorId actorId
) implements StateDelta {
    public ComboAdvanced {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
    }
}