package com.pgalaxyp.fragmento.core.controller.movement;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;

public final class TimedHomingMovement<T extends Entity>
        implements AutoMovementController.Movement<T> {

    @FunctionalInterface
    public interface GoalProvider<T extends Entity> {
        Vec3 goal(T self, LivingEntity target);
    }

    private final int totalTicks;
    private final double maxStep;
    private final GoalProvider<T> goalProvider;

    private int startAge = Integer.MIN_VALUE;

    public TimedHomingMovement(
            int totalTicks,
            double maxStep,
            GoalProvider<T> goalProvider
    ) {
        this.totalTicks = Math.max(1, totalTicks);
        this.maxStep = Math.max(0.0, maxStep);
        this.goalProvider = goalProvider;
    }

    @Override
    public void apply(T self, LivingEntity target, int age) {
        if (self == null || target == null || !target.isAlive()) {
            if (self != null) self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        if (startAge == Integer.MIN_VALUE) {
            startAge = age;
        }

        int elapsed = age - startAge;
        int remaining = totalTicks - elapsed;

        if (remaining <= 0) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 goal = goalProvider.goal(self, target);
        if (goal == null) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 delta = goal.subtract(self.position());
        if (delta.lengthSqr() < 1.0E-8) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 step = delta.scale(1.0 / remaining);

        if (maxStep > 0.0) {
            double stepSqr = step.lengthSqr();
            double maxSqr = maxStep * maxStep;
            if (stepSqr > maxSqr) {
                step = step.normalize().scale(maxStep);
            }
        }

        self.setDeltaMovement(step);
    }
}
