package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.system.entity.SkillEntityBase;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class ConstantSpeedHomingMovement<T extends SkillEntityBase>
        implements AutoMovementController.Movement<T> {

    private final double speedPerTick;

    public ConstantSpeedHomingMovement(double speedPerTick) {
        this.speedPerTick = Math.max(0.001, speedPerTick);
    }

    @Override
    public Vec3 desiredVelocity(T self, LivingEntity target) {
        if (self == null || target == null || !target.isAlive()) {
            return Vec3.ZERO;
        }

        Vec3 from = self.position();
        AABB box = target.getBoundingBox();

        double tx = clamp(from.x, box.minX, box.maxX);
        double ty = clamp(from.y, box.minY, box.maxY);
        double tz = clamp(from.z, box.minZ, box.maxZ);

        Vec3 closest = new Vec3(tx, ty, tz);
        Vec3 delta = closest.subtract(from);

        double dist = delta.length();
        if (dist <= 1.0E-8) {
            return Vec3.ZERO;
        }

        if (dist <= speedPerTick) {
            return delta;
        }

        return delta.scale(speedPerTick / dist);
    }

    private static double clamp(double v, double min, double max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}
