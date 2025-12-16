package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class AutoMovementController<T extends Entity> extends EntityController<T> {

    public interface Movement<T> {
        Vec3 desiredVelocity(T self, LivingEntity target);
    }

    private final Function<T, LivingEntity> targetGetter;

    private Movement<T> movement;
    private boolean enabled = true;

    private double maxSpeedPerTick = 0.65;
    private double accelPerTick = 0.45;

    public AutoMovementController(
            T entity,
            Function<T, LivingEntity> targetGetter
    ) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setMovement(Movement<T> movement) {
        this.movement = movement;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        if (!enabled) {
            entity.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (!enabled) return;
        if (movement == null) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        if (target == null || !target.isAlive()) return;

        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 toTarget = targetCenter.subtract(entity.position());

        double dist = toTarget.length();
        if (dist <= maxSpeedPerTick) {
            entity.setPos(targetCenter.x, targetCenter.y, targetCenter.z);
            entity.setDeltaMovement(Vec3.ZERO);
            return;
        }

        Vec3 desired = movement.desiredVelocity(entity, target);
        if (desired == null) return;

        desired = clampLength(desired, maxSpeedPerTick);

        Vec3 cur = entity.getDeltaMovement();
        Vec3 deltaV = desired.subtract(cur);

        double deltaLen = deltaV.length();
        if (deltaLen > accelPerTick && deltaLen > 0.00000001) {
            deltaV = deltaV.scale(accelPerTick / deltaLen);
        }

        Vec3 nextVel = cur.add(deltaV);
        entity.setDeltaMovement(nextVel);
    }

    private static Vec3 clampLength(Vec3 v, double maxLen) {
        double len = v.length();
        if (len <= maxLen) return v;
        if (len <= 0.00000001) return Vec3.ZERO;
        return v.scale(maxLen / len);
    }
}
