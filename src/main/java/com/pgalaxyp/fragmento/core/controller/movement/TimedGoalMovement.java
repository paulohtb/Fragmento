package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.controller.FlightController;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class TimedGoalMovement<T extends Entity> implements FlightController.Movement<T> {

    @FunctionalInterface
    public interface GoalProvider {
        Vec3 goal();
    }

    private final int totalTicks;
    private final GoalProvider goalProvider;
    private final double factorPerTick;

    private int startAge = Integer.MIN_VALUE;

    public TimedGoalMovement(int totalTicks, GoalProvider goalProvider, double factorPerTick) {
        this.totalTicks = Math.max(1, totalTicks);
        this.goalProvider = goalProvider;
        this.factorPerTick = Mth.clamp(factorPerTick, 0.0, 1.0);
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

        if (goalProvider == null) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 goal = goalProvider.goal();
        if (goal == null) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 delta = goal.subtract(self.position());
        if (delta.lengthSqr() < 1.0E-8) {
            self.setDeltaMovement(Vec3.ZERO);
            return;
        }

        self.setDeltaMovement(delta.scale(factorPerTick));
    }
}
