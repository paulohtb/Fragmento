package com.pgalaxyp.fragmento.combat.actor.port;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
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