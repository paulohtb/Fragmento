package com.pgalaxyp.fragmento.combat.targeting.port;

import com.pgalaxyp.fragmento.combat.actor.api.ActorId;
import com.pgalaxyp.fragmento.combat.targeting.api.Vec3d;

public record RaycastEntityHit(ActorId actorId, Vec3d hitPosition, double distance) implements RaycastHit {
    public RaycastEntityHit {
        if (actorId == null || hitPosition == null) throw new IllegalArgumentException();
        if (!Double.isFinite(distance) || distance < 0.0) throw new IllegalArgumentException();
    }
}