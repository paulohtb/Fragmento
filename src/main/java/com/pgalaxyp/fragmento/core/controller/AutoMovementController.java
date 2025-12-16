package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;

public final class AutoMovementController<T extends net.minecraft.world.entity.Entity>
        extends EntityController<T> {

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setMaxSpeedPerTick(double v) {
        maxSpeedPerTick = Math.max(0.001, v);
    }

    public void setAccelPerTick(double v) {
        accelPerTick = Math.max(0.001, v);
    }

    public double getMaxSpeedPerTick() {
        return maxSpeedPerTick;
    }

    public double getAccelPerTick() {
        return accelPerTick;
    }

    @Override
    protected void onTick() {
        if (entity.level().isClientSide()) return;
        if (!enabled) return;
        if (movement == null) return;

        LivingEntity target = targetGetter != null ? targetGetter.apply(entity) : null;

        Vec3 desired = movement.desiredVelocity(entity, target);
        if (desired == null) return;

        desired = clampLength(desired, maxSpeedPerTick);

        Vec3 cur = entity.getDeltaMovement();
        Vec3 deltaV = desired.subtract(cur);

        double deltaLen = deltaV.length();
        if (deltaLen > accelPerTick) {
            if (deltaLen > 0.00000001) {
                deltaV = deltaV.scale(accelPerTick / deltaLen);
            } else {
                deltaV = Vec3.ZERO;
            }
        }

        Vec3 nextVel = cur.add(deltaV);

        if (nextVel.lengthSqr() < 0.000000000001) {
            entity.setDeltaMovement(Vec3.ZERO);
            return;
        }

        entity.setDeltaMovement(nextVel);
        entity.move(MoverType.SELF, nextVel);
    }

    private static Vec3 clampLength(Vec3 v, double maxLen) {
        double len = v.length();
        if (len <= maxLen) return v;
        if (len <= 0.00000001) return Vec3.ZERO;
        return v.scale(maxLen / len);
    }
}
