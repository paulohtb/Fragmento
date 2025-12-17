package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public final class TimeboxedPositionMovement<T extends Entity> implements AutoMovementController.Movement<T> {

    private final IntSupplier remainingTicks;
    private final Supplier<Vec3> desiredPosition;
    private final double maxSpeedPerTick;

    public TimeboxedPositionMovement(IntSupplier remainingTicks, Supplier<Vec3> desiredPosition, double maxSpeedPerTick) {
        this.remainingTicks = remainingTicks;
        this.desiredPosition = desiredPosition;
        this.maxSpeedPerTick = Math.max(0.001, maxSpeedPerTick);
    }

    @Override
    public Vec3 desiredVelocity(T self, LivingEntity target) {
        if (self == null) return Vec3.ZERO;

        Vec3 dest = desiredPosition != null ? desiredPosition.get() : null;
        if (dest == null) return Vec3.ZERO;

        Vec3 delta = dest.subtract(self.position());

        double distSqr = delta.lengthSqr();
        if (distSqr <= 0.00000001) {
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
        if (len <= 0.00000001) return Vec3.ZERO;
        return v.scale(maxLen / len);
    }
}