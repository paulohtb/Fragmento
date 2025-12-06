package com.pgalaxyp.fragmento.feature.bard.common.spirit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class SpiritTargetBase extends SpiritBase {

    private static final EntityDataAccessor<Boolean> DATA_HAS_HIT =
            SynchedEntityData.defineId(SpiritTargetBase.class, EntityDataSerializers.BOOLEAN);

    private LivingEntity target;
    private boolean spawnSoundPlayed;
    private int flightDuration;
    private double collisionRadius = 0.2D;
    private int moveStartAge;
    private int despawnDurationTicks;
    private int despawnTicks;

    protected SpiritTargetBase(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    public void setMoveStartAge(int age) {
        this.moveStartAge = age;
    }

    public void setDespawnDurationTicks(int ticks) {
        this.despawnDurationTicks = Math.max(0, ticks);
    }

    public int getDespawnDurationTicks() {
        return this.despawnDurationTicks;
    }

    public boolean hasHit() {
        return this.entityData.get(DATA_HAS_HIT);
    }

    private void setHasHit(boolean value) {
        this.entityData.set(DATA_HAS_HIT, value);
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
        if (!this.ensureValidTarget()) {
            return;
        }

        Vec3 targetPos = this.getTargetPosition();

        if (!this.hasHit()) {
            this.lookAtTarget(targetPos);
        }

        if (this.level().isClientSide()) {
            return;
        }

        if (this.hasHit()) {
            this.handleDespawnAfterHit();
            return;
        }

        this.playSpawnSoundOnce();

        int age = this.getLifetime();
        if (age < this.moveStartAge) {
            this.setVelocity(Vec3.ZERO);
            return;
        }

        this.updateMovementTowards(targetPos);
        this.checkCollisionWithTarget();
    }

    public Vec3 getTargetPosition() {
        LivingEntity t = this.getTarget();
        if (t == null) {
            return this.position();
        }
        return t.getBoundingBox().getCenter();
    }

    protected boolean canHitEntity(LivingEntity entity) {
        return true;
    }

    protected abstract void onTargetHit(LivingEntity target);

    protected void onFlightFinished() {
    }

    private boolean ensureValidTarget() {
        LivingEntity logicTarget = this.getTarget();
        if (logicTarget == null || !logicTarget.isAlive()) {
            if (!this.level().isClientSide()) {
                this.discard();
            }
            return false;
        }
        return true;
    }

    private void playSpawnSoundOnce() {
        if (this.spawnSoundPlayed) {
            return;
        }
        this.playSpiritSpawnSound();
        this.spawnSoundPlayed = true;
    }

    private void handleDespawnAfterHit() {
        if (this.despawnDurationTicks <= 0) {
            this.onFlightFinished();
            this.discard();
            return;
        }
        this.despawnTicks++;
        if (this.despawnTicks >= this.despawnDurationTicks) {
            this.onFlightFinished();
            this.discard();
        }
    }

    private void updateMovementTowards(Vec3 targetPos) {
        Vec3 currentPos = this.position();
        Vec3 toTarget = targetPos.subtract(currentPos);

        if (toTarget.lengthSqr() > 0.000001D) {
            int age = this.getLifetime();
            int flightTicksPassed = Math.max(0, age - this.moveStartAge);
            int ticksRemaining = Math.max(1, this.flightDuration - flightTicksPassed);

            double distance = toTarget.length();
            double minSpeedPerTick = distance / 5.0D;

            Vec3 dirNorm = toTarget.normalize();
            double speed = Math.max(distance / ticksRemaining, minSpeedPerTick);
            Vec3 velocity = dirNorm.scale(speed);

            this.setVelocity(velocity);
        } else {
            this.setVelocity(Vec3.ZERO);
        }
    }

    private void checkCollisionWithTarget() {
        LivingEntity t = this.getTarget();
        if (t == null) {
            return;
        }

        if (t == this.getOwner()) {
            return;
        }

        if (!this.canHitEntity(t)) {
            return;
        }

        Vec3 start = new Vec3(this.xOld, this.yOld, this.zOld);
        Vec3 end = this.position();
        AABB pathBox = new AABB(start, end).inflate(this.collisionRadius);

        if (!t.getBoundingBox().intersects(pathBox)) {
            return;
        }

        this.onTargetHit(t);
        this.playSpiritHitSound();
        this.setHasHit(true);
        this.setVelocity(Vec3.ZERO);
        this.despawnTicks = 0;
    }

    protected void lookAtTarget(Vec3 targetPos) {
        Vec3 myPos = this.position();
        Vec3 dir = targetPos.subtract(myPos);

        if (dir.lengthSqr() < 0.000001D) {
            return;
        }

        double dx = dir.x;
        double dy = dir.y;
        double dz = dir.z;

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float)(Math.toDegrees(Math.atan2(dx, dz)));
        float targetPitch = (float)(Math.toDegrees(Math.atan2(-dy, horizontal)));

        float maxTurnPerTick = 20.0F;

        float newYaw = Mth.approachDegrees(this.getYRot(), targetYaw, maxTurnPerTick);
        float newPitch = Mth.approachDegrees(this.getXRot(), targetPitch, maxTurnPerTick);

        this.setYRot(newYaw);
        this.yRotO = newYaw;

        this.setXRot(newPitch);
        this.xRotO = newPitch;
    }

    public void faceInstantlyTowards(Vec3 targetPos) {
        Vec3 myPos = this.position();
        Vec3 dir = targetPos.subtract(myPos);

        if (dir.lengthSqr() < 0.000001D) {
            return;
        }

        double dx = dir.x;
        double dy = dir.y;
        double dz = dir.z;

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float)(Math.toDegrees(Math.atan2(dx, dz)));
        float targetPitch = (float)(Math.toDegrees(Math.atan2(-dy, horizontal)));

        this.setYRot(targetYaw);
        this.yRotO = targetYaw;

        this.setXRot(targetPitch);
        this.xRotO = targetPitch;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HAS_HIT, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.spawnSoundPlayed = tag.getBoolean("SpawnSoundPlayed");
        this.flightDuration = tag.getInt("FlightDuration");
        this.collisionRadius = tag.getDouble("CollisionRadius");
        this.moveStartAge = tag.getInt("MoveStartAge");
        this.despawnDurationTicks = tag.getInt("DespawnDuration");
        this.despawnTicks = tag.getInt("DespawnTicks");
        this.setHasHit(tag.getBoolean("HasHit"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("SpawnSoundPlayed", this.spawnSoundPlayed);
        tag.putInt("FlightDuration", this.flightDuration);
        tag.putDouble("CollisionRadius", this.collisionRadius);
        tag.putInt("MoveStartAge", this.moveStartAge);
        tag.putInt("DespawnDuration", this.despawnDurationTicks);
        tag.putInt("DespawnTicks", this.despawnTicks);
        tag.putBoolean("HasHit", this.hasHit());
    }
}
