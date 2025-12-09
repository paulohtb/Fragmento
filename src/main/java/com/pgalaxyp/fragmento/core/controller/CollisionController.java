package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class CollisionController<T extends Entity> extends EntityController<T> {

    @FunctionalInterface
    public interface CollisionCheck<T extends Entity> {
        LivingEntity collided(T self, LivingEntity target);
    }

    private CollisionCheck<T> check;
    private boolean enabled = true;
    private LivingEntity lastCollision;

    private final Function<T, LivingEntity> targetGetter;

    public CollisionController(T entity, Function<T, LivingEntity> targetGetter) {
        super(entity);
        this.targetGetter = targetGetter;
    }

    public void setEnabled(boolean v) {
        enabled = v;
        if (!v) {
            lastCollision = null;
            entity.setDeltaMovement(Vec3.ZERO);
        }
    }

    public void setCollisionCheck(CollisionCheck<T> check) {
        this.check = check;
    }

    public boolean hasCollision() {
        return lastCollision != null;
    }

    public LivingEntity getCollisionTarget() {
        return lastCollision;
    }

    public void resetCollision() {
        lastCollision = null;
    }

    @Override
    public void tick() {
        if (!enabled) return;
        if (check == null) return;

        LivingEntity target = targetGetter.apply(entity);

        if (target != null && target.isAlive()) {
            LivingEntity result = check.collided(entity, target);
            if (result != null) {
                lastCollision = result;
                return;
            }
        }

        AABB area = entity.getBoundingBox().inflate(0.5);

        for (Entity e : entity.level().getEntities(entity, area, x -> x instanceof LivingEntity l && l.isAlive())) {
            LivingEntity candidate = (LivingEntity) e;
            LivingEntity result = check.collided(entity, candidate);
            if (result != null) {
                lastCollision = result;
                return;
            }
        }
    }

    public CollisionCheck<T> surfaceHitboxCollision(double inflate) {
        return (self, target) -> {
            AABB expanded = target.getBoundingBox().inflate(inflate);
            AABB box = self.getBoundingBox();
            return expanded.intersects(box) ? target : null;
        };
    }

    public CollisionCheck<T> centerDistanceCollision(double maxDistance) {
        return (self, target) -> {
            Vec3 a = self.getBoundingBox().getCenter();
            Vec3 b = target.getBoundingBox().getCenter();
            double d = a.distanceTo(b);
            return d <= maxDistance ? target : null;
        };
    }
}
