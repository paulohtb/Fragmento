package com.pgalaxyp.fragmento.feature.bard.common.spirit;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class TargetedSpiritBase extends SpiritBase {

    private LivingEntity target;
    private boolean spawnSoundPlayed;
    private int flightTicks;
    private int flightDuration;
    private double collisionRadius = 0.2D;

    protected TargetedSpiritBase(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    public void configureFlight(int durationTicks, double collisionRadius) {
        if (durationTicks < 1) {
            durationTicks = 1;
        }
        this.flightDuration = durationTicks;
        if (collisionRadius > 0.0D) {
            this.collisionRadius = collisionRadius;
        }
    }

    @Override
    protected void onSpiritTick() {
        if (this.level().isClientSide()) {
            return;
        }

        if (this.target == null || !this.target.isAlive()) {
            this.discard();
            return;
        }

        if (!this.spawnSoundPlayed) {
            this.playSpiritSpawnSound();
            this.spawnSoundPlayed = true;
        }

        if (this.flightDuration <= 0) {
            this.discard();
            return;
        }

        this.flightTicks++;

        Vec3 currentPos = this.position();
        Vec3 targetPos = this.getTargetPosition();
        int remaining = this.flightDuration - this.flightTicks + 1;
        if (remaining < 1) {
            remaining = 1;
        }

        Vec3 toTarget = targetPos.subtract(currentPos);
        if (toTarget.lengthSqr() > 1.0E-6D) {
            Vec3 velocity = toTarget.scale(1.0D / remaining);
            this.setVelocity(velocity);
        }

        Vec3 start = new Vec3(this.xOld, this.yOld, this.zOld);
        Vec3 end = this.position();
        AABB box = new AABB(start, end).inflate(this.collisionRadius);

        for (LivingEntity candidate : this.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (candidate == this.getOwner()) {
                continue;
            }
            if (!this.canHitEntity(candidate)) {
                continue;
            }
            this.onTargetHit(candidate);
            this.playSpiritHitSound();
            this.discard();
            return;
        }

        if (this.flightTicks > this.flightDuration + 2) {
            this.onFlightFinished();
            this.discard();
        }
    }

    protected Vec3 getTargetPosition() {
        return this.target.getBoundingBox().getCenter();
    }

    protected boolean canHitEntity(LivingEntity entity) {
        return true;
    }

    protected abstract void onTargetHit(LivingEntity target);

    protected void onFlightFinished() {
    }
}
