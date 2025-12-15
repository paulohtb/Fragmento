package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class FlightController<T extends Entity> extends EntityController<T> {

    public interface Movement<T extends Entity> {
        void apply(T self, LivingEntity target, int age);
    }

    private Movement<T> movement;
    private boolean enabled = true;

    private int zeroTicks;

    private final Function<T, LivingEntity> targetGetter;
    private final Function<T, Integer> ageGetter;

    public FlightController(
            T entity,
            Function<T, LivingEntity> targetGetter,
            Function<T, Integer> ageGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
        this.ageGetter = ageGetter;
    }

    public void setMovement(Movement<T> movement) {
        this.movement = movement;
        zeroTicks = 0;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        zeroTicks = 0;
    }

    @Override
    protected void onTick() {
        if (!enabled || movement == null) return;

        LivingEntity target = targetGetter.apply(entity);

        Vec3 before = entity.getDeltaMovement();
        movement.apply(entity, target, ageGetter.apply(entity));
        Vec3 after = entity.getDeltaMovement();

        if (after.lengthSqr() < 1.0E-8) {
            if (++zeroTicks >= 3) {
                enabled = false;
            }
        } else {
            zeroTicks = 0;
        }
    }
}
