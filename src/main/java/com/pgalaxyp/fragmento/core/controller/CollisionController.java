package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class CollisionController<T extends Entity>
        extends EntityController<T> {

    @FunctionalInterface
    public interface CollisionCheck<T extends Entity> {
        LivingEntity collided(T self, LivingEntity target);
    }

    private CollisionCheck<T> check;
    private boolean enabled = true;

    private LivingEntity lastCollision;
    private Vec3 correctedMotion;

    private int scanIntervalTicks = 1;
    private int scanCounter;

    private boolean scanOtherEntities = true;

    private final Function<T, LivingEntity> targetGetter;

    public CollisionController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        if (!value) {
            lastCollision = null;
            correctedMotion = null;
        }
    }

    public void setCollisionCheck(CollisionCheck<T> check) {
        this.check = check;
    }

    public void setScanIntervalTicks(int ticks) {
        scanIntervalTicks = Math.max(1, ticks);
        scanCounter = 0;
    }

    public void setScanOtherEntities(boolean value) {
        scanOtherEntities = value;
    }

    public boolean hasCollision() {
        return lastCollision != null;
    }

    public LivingEntity getCollisionTarget() {
        return lastCollision;
    }

    public Vec3 getCorrectedMotionOrNull() {
        return correctedMotion;
    }

    public void resetCollision() {
        lastCollision = null;
        correctedMotion = null;
    }

    @Override
    protected void onTick() {
        if (!enabled || check == null) return;
        if (lastCollision != null) return;

        scanCounter++;
        if (scanCounter < scanIntervalTicks) return;
        scanCounter = 0;

        LivingEntity target = targetGetter.apply(entity);
        if (target != null && target.isAlive()) {
            LivingEntity result = check.collided(entity, target);
            if (result != null) {
                lastCollision = result;
                return;
            }
        }

        if (!scanOtherEntities) return;

        AABB area = entity.getBoundingBox().inflate(0.75);

        Predicate<Entity> filter = e -> {
            if (!(e instanceof LivingEntity l)) return false;
            return l.isAlive();
        };

        List<Entity> list = entity.level().getEntities(entity, area, filter);
        int size = list.size();

        for (Entity e : list) {
            if (!(e instanceof LivingEntity l)) continue;

            LivingEntity result = check.collided(entity, l);
            if (result != null) {
                lastCollision = result;
                return;
            }
        }
    }

    public CollisionCheck<T> sweptStopBeforeHitbox(double inflate) {
        return (self, target) -> {
            Vec3 motion = self.getDeltaMovement();
            if (motion.lengthSqr() < 1.0E-10) return null;

            AABB start = self.getBoundingBox();
            AABB targetBox = target.getBoundingBox().inflate(inflate);

            AABB swept = start.expandTowards(motion).inflate(inflate);
            if (!targetBox.intersects(swept)) return null;

            Vec3 corrected = resolveNonPenetratingMotion(start, targetBox, motion);
            correctedMotion = corrected;

            self.setDeltaMovement(corrected);
            return target;
        };
    }

    public CollisionCheck<T> sweptDetectOnly(double inflate) {
        return (self, target) -> {
            Vec3 motion = self.getDeltaMovement();
            if (motion.lengthSqr() < 1.0E-10) return null;

            AABB start = self.getBoundingBox();
            AABB targetBox = target.getBoundingBox().inflate(inflate);

            AABB swept = start.expandTowards(motion).inflate(inflate);
            if (!targetBox.intersects(swept)) return null;

            correctedMotion = null;
            return target;
        };
    }

    private static Vec3 resolveNonPenetratingMotion(
            AABB start,
            AABB target,
            Vec3 motion
    ) {
        double lo = 0.0;
        double hi = 1.0;

        for (int i = 0; i < 7; i++) {
            double mid = (lo + hi) * 0.5;
            AABB moved = start.move(motion.scale(mid));
            if (moved.intersects(target)) {
                hi = mid;
            } else {
                lo = mid;
            }
        }

        double safe = Math.max(0.0, lo - 0.01);
        if (safe <= 0.0) return Vec3.ZERO;

        return motion.scale(safe);
    }
}
