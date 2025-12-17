package com.pgalaxyp.fragmento.core.controller;

import java.util.function.Function;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class AutoMovementController<T extends Entity> extends EntityController<T> {

    public interface Movement<T> {
        Vec3 desiredVelocity(T self, LivingEntity target);
    }

    private final Function<T, LivingEntity> targetGetter;

    private Movement<T> movement;
    private boolean enabled = true;

    private double maxSpeedPerTick = 0.65;
    private double accelPerTick = 0.45;

    private boolean snapToDesired;

    public AutoMovementController(T entity, Function<T, LivingEntity> targetGetter) {
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

    public void setMaxSpeedPerTick(double value) {
        maxSpeedPerTick = Math.max(0.0, value);
    }

    public void setAccelPerTick(double value) {
        accelPerTick = Math.max(0.0, value);
    }

    public void setSnapToDesired(boolean value) {
        snapToDesired = value;
    }

    public double getMaxSpeedPerTick() {
        return maxSpeedPerTick;
    }

    public double getAccelPerTick() {
        return accelPerTick;
    }

    public boolean isSnapToDesired() {
        return snapToDesired;
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (!enabled) return;
        if (movement == null) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;
        if (target == null || !target.isAlive()) return;

        Vec3 desired = movement.desiredVelocity(entity, target);
        if (desired == null) return;

        desired = clampLength(desired, maxSpeedPerTick);

        if (snapToDesired) {
            entity.setDeltaMovement(desired);
            return;
        }

        Vec3 cur = entity.getDeltaMovement();

        double accel = accelPerTick;
        if (accel <= 0.0) {
            entity.setDeltaMovement(desired);
            return;
        }

        Vec3 deltaV = desired.subtract(cur);
        double deltaLen = deltaV.length();

        if (deltaLen <= accel) {
            entity.setDeltaMovement(desired);
            return;
        }

        if (deltaLen <= 0.00000001) {
            entity.setDeltaMovement(cur);
            return;
        }

        Vec3 nextVel = cur.add(deltaV.scale(accel / deltaLen));
        entity.setDeltaMovement(nextVel);
    }

    private static Vec3 clampLength(Vec3 v, double maxLen) {
        if (v == null) return Vec3.ZERO;
        if (maxLen <= 0.0) return Vec3.ZERO;

        double len = v.length();
        if (len <= maxLen) return v;
        if (len <= 0.00000001) return Vec3.ZERO;
        return v.scale(maxLen / len);
    }
}
