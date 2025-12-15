package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.util.MathUtil;
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
    private final GoalProvider<T> goalProvider;
    private int startAge = Integer.MIN_VALUE;

    public TimedHomingMovement(int totalTicks, GoalProvider<T> goalProvider) {
        this.totalTicks = Math.max(1, totalTicks);
        this.goalProvider = goalProvider;
    }

    @Override
    public Vec3 position(T self, LivingEntity target, int age) {
        if (self == null || target == null || !target.isAlive()) {
            return self.position();
        }

        if (startAge == Integer.MIN_VALUE) startAge = age;

        int elapsed = age - startAge;
        int remaining = totalTicks - elapsed;
        if (remaining <= 0) return self.position();

        Vec3 goal = goalProvider.goal(self, target);
        Vec3 cur = self.position();

        return MathUtil.lerp(cur, goal, 1.0 / remaining);
    }
}
