package com.pgalaxyp.fragmento.combat.actorModule.api;

import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.Objects;

public record ActorState(ClassId classId, int healthHearts, int maxHealthHearts) {
    public ActorState {
        Objects.requireNonNull(classId);
        if (healthHearts < 0 || maxHealthHearts <= 0 || healthHearts > maxHealthHearts) throw new IllegalArgumentException();
    }
}