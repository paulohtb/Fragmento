package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import java.util.function.IntSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class TimeboxedHomingMovement<T extends SkillEntityBase>
        implements AutoMovementController.Movement<T> {

    private final IntSupplier remainingTicks;
    private final double maxSpeedPerTick;

    public TimeboxedHomingMovement(IntSupplier remainingTicks, double maxSpeedPerTick) {
        this.remainingTicks = remainingTicks;
        this.maxSpeedPerTick = Math.max(0.001, maxSpeedPerTick);
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

        double distSqr = delta.lengthSqr();
        if (distSqr <= 1.0E-8) {
            return Vec3.ZERO;
        }

        int rem = remainingTicks != null ? remainingTicks.getAsInt() : 1;
        if (rem <= 0) rem = 1;

        if (rem == 1) {
            return clampLength(delta, maxSpeedPerTick);
        }

        Vec3 step = delta.scale(1.0 / (double) rem);
        return clampLength(step, maxSpeedPerTick);
    }

    private static Vec3 clampLength(Vec3 v, double maxLen) {
        double len = v.length();
        if (len <= maxLen) return v;
        if (len <= 1.0E-8) return Vec3.ZERO;
        return v.scale(maxLen / len);
    }

    private static double clamp(double v, double min, double max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }
}
