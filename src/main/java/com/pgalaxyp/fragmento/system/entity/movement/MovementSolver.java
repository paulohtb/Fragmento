package com.pgalaxyp.fragmento.system.entity.movement;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class MovementSolver {

    private MovementSolver() {
    }

    public static Vec3 resolveVelocity(Entity self, MovementPlan plan) {
        if (plan == null) return Vec3.ZERO;
        if (plan.kind != MovementPlan.Kind.VELOCITY) return Vec3.ZERO;
        return plan.desiredVelocity != null ? plan.desiredVelocity : Vec3.ZERO;
    }
}