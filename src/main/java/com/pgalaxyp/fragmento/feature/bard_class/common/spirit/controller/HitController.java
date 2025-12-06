package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class HitController<T extends SpiritBase> extends SpiritController<T> {

    private final TargetController<?> targetCtrl;
    private final FlightController<?> flightCtrl;

    private boolean hasHit;
    private int despawnTicks;
    private int despawnDuration;
    private boolean enabled = true;

    public HitController(T spirit, TargetController<?> target, FlightController<?> flight) {
        super(spirit);
        this.targetCtrl = target;
        this.flightCtrl = flight;
    }

    public void setDespawnDuration(int ticks) {
        this.despawnDuration = ticks;
    }

    public boolean hasHit() {
        return this.hasHit;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            spirit.setVelocity(Vec3.ZERO);
        }
    }

    @Override
    public void tick() {
        if (!enabled) {
            return;
        }

        LivingEntity target = targetCtrl.getTarget();

        if (hasHit) {
            despawnTicks++;
            if (despawnTicks >= despawnDuration) {
                onFlightFinished();
                spirit.discard();
            }
            return;
        }

        if (target == null || !target.isAlive()) {
            hasHit = true;
            flightCtrl.setEnabled(false);
            spirit.setVelocity(Vec3.ZERO);
            onTargetLost();
            return;
        }

        checkCollision(target);
    }

    private void checkCollision(LivingEntity target) {
        Vec3 start = new Vec3(spirit.xOld, spirit.yOld, spirit.zOld);
        Vec3 end = spirit.position();

        AABB path = new AABB(start, end).inflate(flightCtrl.getCollisionRadius());
        if (!target.getBoundingBox().intersects(path)) {
            return;
        }

        hasHit = true;
        flightCtrl.setEnabled(false);
        spirit.setVelocity(Vec3.ZERO);
        onTargetHit(target);
    }

    protected abstract void onTargetHit(LivingEntity target);

    protected void onTargetLost() {
    }

    protected void onFlightFinished() {
    }
}
