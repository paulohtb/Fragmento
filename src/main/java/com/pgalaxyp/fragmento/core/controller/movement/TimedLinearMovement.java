package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class TimedLinearMovement<T extends Entity>
        implements AutoMovementController.Movement<T> {

    private final int totalTicks;
    private final Vec3 direction;
    private int startAge = Integer.MIN_VALUE;

    public TimedLinearMovement(int totalTicks, Vec3 direction) {
        this.totalTicks = Math.max(1, totalTicks);
        this.direction = direction == null ? Vec3.ZERO : direction;
    }

    @Override
    public Vec3 position(T self, LivingEntity target, int age) {
        if (self == null) return Vec3.ZERO;

        if (startAge == Integer.MIN_VALUE) startAge = age;

        int elapsed = age - startAge;
        if (elapsed >= totalTicks) return self.position();

        return self.position().add(direction);
    }
}
