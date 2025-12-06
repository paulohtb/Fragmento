package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.world.phys.Vec3;

public final class FlightController<T extends SpiritBase> extends SpiritController<T> {

    private final TargetController<?> targetCtrl;
    private int moveStartAge;
    private int flightDuration;
    private double collisionRadius;
    private boolean enabled = true;

    public FlightController(T spirit, TargetController<?> targetCtrl) {
        super(spirit);
        this.targetCtrl = targetCtrl;
    }

    public void configure(int idleTicks, int travelTicks, double collisionRadius) {
        this.moveStartAge = idleTicks;
        this.flightDuration = travelTicks;
        this.collisionRadius = collisionRadius;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            spirit.setVelocity(Vec3.ZERO);
        }
    }

    @Override
    public void tick() {
        if (!enabled) return;
        if (!targetCtrl.hasValidTarget()) return;

        int age = spirit.getLifetime();
        if (age < moveStartAge) {
            spirit.setVelocity(Vec3.ZERO);
            return;
        }

        Vec3 targetPos = targetCtrl.getTarget().getBoundingBox().getCenter();
        Vec3 current = spirit.position();
        Vec3 diff = targetPos.subtract(current);

        if (diff.lengthSqr() < 1e-7) {
            spirit.setVelocity(Vec3.ZERO);
            return;
        }

        int passed = age - moveStartAge;
        int remain = Math.max(1, flightDuration - passed);
        double speed = diff.length() / remain;
        Vec3 vel = diff.normalize().scale(speed);
        spirit.setVelocity(vel);
    }

    public double getCollisionRadius() {
        return collisionRadius;
    }
}
