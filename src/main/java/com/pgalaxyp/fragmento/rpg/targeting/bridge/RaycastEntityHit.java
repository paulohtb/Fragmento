package com.pgalaxyp.fragmento.rpg.targeting.bridge;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public record RaycastEntityHit(ActorId actorId, Vec3d hitPosition, double distance) implements RaycastHit {

    public RaycastEntityHit {
        if (actorId == null || hitPosition == null) {
            throw new IllegalArgumentException();
        }
        if (!Double.isFinite(distance) || distance < 0.0) {
            throw new IllegalArgumentException();
        }
    }
}