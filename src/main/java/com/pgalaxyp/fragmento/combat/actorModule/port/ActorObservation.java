package com.pgalaxyp.fragmento.combat.actorModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import java.util.Objects;

public record ActorObservation(ActorId actorId, ClassId classId, int healthHearts, int maxHealthHearts) {
    public ActorObservation {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(classId);
        if (healthHearts < 0) throw new IllegalArgumentException();
        if (maxHealthHearts <= 0) throw new IllegalArgumentException();
        if (healthHearts > maxHealthHearts) throw new IllegalArgumentException();
    }
}