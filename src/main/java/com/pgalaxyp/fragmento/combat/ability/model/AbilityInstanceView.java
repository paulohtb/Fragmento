package com.pgalaxyp.fragmento.combat.ability.model;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record AbilityInstanceView(
        AbilityId abilityId,
        ActorId actorId,
        long startFrame,
        long endFrame
) {
    public AbilityInstanceView {
        Objects.requireNonNull(abilityId);
        Objects.requireNonNull(actorId);
        if (startFrame < 0 || endFrame <= startFrame) throw new IllegalArgumentException();
    }

    public static AbilityInstanceView of(AbilityInstance i) {
        return new AbilityInstanceView(i.abilityId(), i.actorId(), i.startFrame(), i.endFrame());
    }

    public boolean activeAt(long frame) { return frame >= startFrame && frame < endFrame; }
}
