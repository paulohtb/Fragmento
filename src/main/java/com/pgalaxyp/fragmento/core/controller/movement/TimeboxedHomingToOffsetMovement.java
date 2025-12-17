package com.pgalaxyp.fragmento.core.controller.movement;

import java.util.function.IntSupplier;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.system.entity.SkillEntityBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class TimeboxedHomingToOffsetMovement<T extends SkillEntityBase> implements AutoMovementController.Movement<T> {

    private final IntSupplier remainingTicks;
    private final double extraStopDistance;

    public TimeboxedHomingToOffsetMovement(IntSupplier remainingTicks, double extraStopDistance) {
        this.remainingTicks = remainingTicks;
        this.extraStopDistance = Math.max(0.0, extraStopDistance);
    }

    @Override
    public Vec3 desiredVelocity(T self, LivingEntity target) {
        if (self == null || target == null || !target.isAlive()) return Vec3.ZERO;

        int rem = remainingTicks != null ? remainingTicks.getAsInt() : 1;
        if (rem <= 0) rem = 1;

        Vec3 center = target.getBoundingBox().getCenter();

        double halfTarget = 0.5 * Math.max(target.getBbWidth(), target.getBbHeight());
        double halfSelf = 0.5 * Math.max(self.getBbWidth(), self.getBbHeight());
        double stop = halfTarget + halfSelf + extraStopDistance;

        Vec3 fromCenter = self.position().subtract(center);
        Vec3 dirOut;
        if (fromCenter.lengthSqr() > 1.0E-12) {
            dirOut = fromCenter.normalize();
        } else {
            dirOut = new Vec3(0.0, 0.0, 1.0);
        }

        Vec3 desiredPos = center.add(dirOut.scale(stop));

        Vec3 delta = desiredPos.subtract(self.position());
        double distSqr = delta.lengthSqr();
        if (distSqr <= 1.0E-12) return Vec3.ZERO;

        if (rem == 1) return delta;
        return delta.scale(1.0 / (double) rem);
    }
}