package com.pgalaxyp.fragmento.combat.delta;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public record CooldownStarted(
        ActorId actorId,
        AbilityId abilityId,
        long endFrame
) implements StateDelta {
    public CooldownStarted {
        if (actorId == null || abilityId == null) throw new IllegalArgumentException();
        if (endFrame < 0) throw new IllegalArgumentException();
    }
}