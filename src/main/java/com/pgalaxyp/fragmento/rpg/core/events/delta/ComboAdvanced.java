package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;

public record ComboAdvanced(
        ActorId actorId,
        long stepFrameId
) implements StateDelta {
    public ComboAdvanced {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        if (stepFrameId < 0) {
            throw new IllegalArgumentException();
        }
    }
}