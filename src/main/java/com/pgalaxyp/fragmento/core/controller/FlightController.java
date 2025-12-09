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
        if (!v) entity.setDeltaMovement(Vec3.ZERO);
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

    public Movement<T> dashMovement(int totalTicks) {
        return (self, target, age) -> {
            Vec3 from = self.position();
            Vec3 to = target.getBoundingBox().getCenter();
            Vec3 delta = to.subtract(from);

            double len = delta.length();
            if (len < 1.0E-6) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            int remain = Math.max(1, totalTicks - age);
            Vec3 vel = delta.scale(1.0 / remain);
            self.setDeltaMovement(vel);
        };
    }

    public Movement<T> orbitMovement(double radius, double angularSpeed, double travelSpeed) {
        return (self, target, age) -> {
            Vec3 center = target.getBoundingBox().getCenter();

            double angle = age * angularSpeed;
            double x = center.x + Math.cos(angle) * radius;
            double z = center.z + Math.sin(angle) * radius;
            double y = center.y;

            Vec3 dest = new Vec3(x, y, z);
            Vec3 delta = dest.subtract(self.position());
            double len = delta.length();

            if (len < 1.0E-6) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            Vec3 vel = delta.normalize().scale(travelSpeed);
            self.setDeltaMovement(vel);
        };
    }
}
