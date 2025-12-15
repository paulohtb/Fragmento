package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.controller.FlightController;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class TimedLinearMovement<T extends Entity> implements FlightController.Movement<T> {

    @FunctionalInterface
    public interface VelocityProvider {
        Vec3 velocity();
    }

    private final int totalTicks;
    private final double maxStep;
    private final VelocityProvider velocityProvider;

    private int startAge = Integer.MIN_VALUE;

    public TimedLinearMovement(int totalTicks, VelocityProvider velocityProvider) {
        this(totalTicks, 0.0, velocityProvider);
    }

    public TimedLinearMovement(int totalTicks, double maxStep, VelocityProvider velocityProvider) {
        this.totalTicks = Math.max(1, totalTicks);
        this.maxStep = Math.max(0.0, maxStep);
        this.velocityProvider = velocityProvider;
    }

    @Override
    public void apply(T self, LivingEntity target, int age) {
        if (self == null) return;

        if (startAge == Integer.MIN_VALUE) {
            startAge = age;
        }

        int elapsed = age - startAge;
        if (elapsed >= totalTicks) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        if (velocityProvider == null) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 v = velocityProvider.velocity();
        if (v == null) v = Vec3.ZERO;

        if (v.lengthSqr() < 1.0E-8) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        if (maxStep > 0.0) {
            double lenSqr = v.lengthSqr();
            double maxSqr = maxStep * maxStep;
            if (lenSqr > maxSqr) {
                v = v.normalize().scale(maxStep);
            }
        }

        self.setDeltaMovement(v);
    }
}
