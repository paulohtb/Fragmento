package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public final class FlightController<T extends Entity> extends EntityController<T> {

    @FunctionalInterface
    public interface Movement<T extends Entity> {
        void apply(T self, LivingEntity target, int age);
    }

    private Movement<T> movement;
    private boolean enabled = true;

    private final Function<T, LivingEntity> targetGetter;
    private final Function<T, Integer> ageGetter;

    public FlightController(T entity, Function<T, LivingEntity> targetGetter, Function<T, Integer> ageGetter) {
        super(entity);
        this.targetGetter = targetGetter;
        this.ageGetter = ageGetter;
    }

    public void setMovement(Movement<T> movement) {
        this.movement = movement;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    @Override
    public void tick() {
        if (!enabled) return;
        if (movement == null) return;

        LivingEntity target = targetGetter.apply(entity);
        if (target == null) return;

        int age = ageGetter.apply(entity);
        movement.apply(entity, target, age);
    }

    public Movement<T> dashMovement(double totalTicks) {
        double startAge = ageGetter.apply(entity);
        double duration = Math.max(1, totalTicks);

        return (self, target, age) -> {
            double elapsed = age - startAge;
            if (elapsed < 0) elapsed = 0;
            if (elapsed >= duration) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 from = self.position();
            Vec3 to = target.getBoundingBox().getCenter();
            Vec3 delta = to.subtract(from);

            double len = delta.length();
            if (len < 1.0E-6) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            double remain = duration - elapsed;
            Vec3 vel = delta.scale(1.0 / remain);
            self.setDeltaMovement(vel);
        };
    }

    public Movement<T> dashCharged(int totalTicks, double speedFactor) {
        int startAge = ageGetter.apply(entity);
        int duration = Math.max(1, totalTicks);

        return (self, target, age) -> {
            int elapsed = age - startAge;
            if (elapsed < 0) elapsed = 0;
            if (elapsed >= duration) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 from = self.position();
            Vec3 to = target.getBoundingBox().getCenter();

            Vec3 dir = to.subtract(from).normalize();
            Vec3 vel = dir.scale(speedFactor);

            self.setDeltaMovement(vel);
        };
    }

    public Movement<T> overshootCharged(double overshootDistance, int overshootTicks) {
        int startAge = ageGetter.apply(entity);
        int duration = Math.max(1, overshootTicks);

        return (self, target, age) -> {
            int elapsed = age - startAge;
            if (elapsed < 0) elapsed = 0;
            if (elapsed >= duration) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 from = self.position();
            Vec3 to = target.getBoundingBox().getCenter();

            Vec3 dir = to.subtract(from).normalize();
            Vec3 finalPos = to.add(dir.scale(overshootDistance));
            Vec3 delta = finalPos.subtract(from);

            int remain = duration - elapsed;
            Vec3 vel = delta.scale(1.0 / remain);

            self.setDeltaMovement(vel);
        };
    }

    public Movement<T> ascendCharged(double riseHeight, int totalTicks) {
        int startAge = ageGetter.apply(entity);
        int duration = Math.max(1, totalTicks);

        return (self, target, age) -> {
            int elapsed = age - startAge;
            if (elapsed < 0) elapsed = 0;
            if (elapsed >= duration) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 from = self.position();
            double targetY = target.getBoundingBox().maxY + riseHeight;

            Vec3 to = new Vec3(from.x, targetY, from.z);
            Vec3 delta = to.subtract(from);

            int remain = duration - elapsed;
            Vec3 vel = delta.scale(1.0 / remain);

            self.setDeltaMovement(vel);
        };
    }

    public Movement<T> hoverCharged(Vec3 offset) {
        return (self, target, age) -> {
            Vec3 base = new Vec3(target.getX(), target.getY(), target.getZ());
            Vec3 desired = base.add(offset);

            Vec3 delta = desired.subtract(self.position());
            self.setDeltaMovement(delta);
        };
    }
}