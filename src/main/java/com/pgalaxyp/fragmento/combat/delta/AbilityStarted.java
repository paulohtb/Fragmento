package com.pgalaxyp.fragmento.combat.delta;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public record AbilityStarted(AbilityId abilityId, ActorId actorId, long startFrame, long endFrame) implements StateDelta {
    public AbilityStarted {
        if (abilityId == null || actorId == null) throw new IllegalArgumentException();
        if (startFrame < 0 || endFrame <= startFrame) throw new IllegalArgumentException();
    }
}
