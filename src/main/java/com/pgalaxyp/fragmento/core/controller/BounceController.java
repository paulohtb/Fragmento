package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class BounceController<T extends Entity> extends EntityController<T> {

    private Vec3 pending;
    private boolean active;
    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;

    public BounceController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        if (!value) {
            pending = null;
            active = false;
        }
    }

    public void bounce() {
        LivingEntity target = targetGetter.apply(entity);
        if (target == null) return;

        Vec3 dir = entity.position().subtract(target.position());
        if (dir.lengthSqr() < 1.0E-6) dir = new Vec3(0, 0.2, 0);

        pending = dir.normalize().add(0, 0.2, 0).scale(0.2);
        active = true;
    }

    @Override
    protected void onTick() {
        if (!enabled || !active) return;
        entity.setDeltaMovement(pending);
        active = false;
    }
}
