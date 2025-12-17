package com.pgalaxyp.fragmento.system.entity.movement;

import net.minecraft.world.phys.Vec3;

public final class MovementPlan {

    public enum Kind {
        NONE,
        VELOCITY
    }

    public Kind kind = Kind.NONE;
    public Vec3 desiredVelocity = Vec3.ZERO;

    public void clear() {
        kind = Kind.NONE;
        desiredVelocity = Vec3.ZERO;
    }
}