package com.pgalaxyp.fragmento.combat.old.system.entity.movement;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class OrientationSolver {

    private OrientationSolver() {}

    public static void apply(Entity self, LookPlan plan) {
        if (self == null || plan == null) return;
        if (plan.kind != LookPlan.Kind.TO_POS) return;
        if (plan.lookAtPos == null) return;

        Vec3 d = plan.lookAtPos.subtract(self.position());
        if (d.lengthSqr() < 1.0E-12) return;

        double yaw = Math.atan2(d.z, d.x) * 57.29577951308232 - 90.0;
        double pitch = -Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z)) * 57.29577951308232;

        self.setYRot((float) yaw);
        self.setXRot((float) pitch);
    }
}