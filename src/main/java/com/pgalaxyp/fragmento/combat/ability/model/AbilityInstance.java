package com.pgalaxyp.fragmento.combat.ability.model;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record AbilityInstance(
        AbilityId abilityId,
        ActorId actorId,
        long startFrame,
        long endFrame
) {
    public AbilityInstance {
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(actorId);
        if (startFrame < 0 || endFrame <= startFrame) throw new IllegalArgumentException();
    }

    public boolean activeAt(long frame) { return frame >= startFrame && frame < endFrame; }
}
