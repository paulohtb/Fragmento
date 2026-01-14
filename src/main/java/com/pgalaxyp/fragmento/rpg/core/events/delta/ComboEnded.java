package com.pgalaxyp.fragmento.rpg.core.events.delta;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record ComboEnded(ActorId actorId, ActionKey actionKey) implements StateDelta {

    public ComboEnded {
        if (actorId == null || actionKey == null) {
            throw new IllegalArgumentException();
        }
    }
}