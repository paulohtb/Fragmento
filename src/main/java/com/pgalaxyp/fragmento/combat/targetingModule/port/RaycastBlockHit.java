package com.pgalaxyp.fragmento.combat.targetingModule.port;

import com.pgalaxyp.fragmento.combat.util.Vec3d;

public record RaycastBlockHit(Vec3d hitPosition, double distance) implements RaycastHit {
    public RaycastBlockHit {
        if (hitPosition == null) throw new IllegalArgumentException();
        if (!Double.isFinite(distance) || distance < 0.0) throw new IllegalArgumentException();
    }
}