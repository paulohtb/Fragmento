package com.pgalaxyp.fragmento.feature.bard.common.spirit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class SpiritBase extends Entity {

    private LivingEntity owner;
    private int lifetime;
    private int maxLifetime;
    private int spawnDelay;

    protected SpiritBase(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    public void setOwner(LivingEntity entity) {
        this.owner = entity;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setMaxLifetime(int ticks) {
        this.maxLifetime = ticks;
    }

    public int getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setSpawnDelay(int ticks) {
        this.spawnDelay = ticks;
    }

    public boolean isSpawnReady() {
        return this.spawnDelay <= 0;
    }

    public boolean isExpired() {
        return this.maxLifetime > 0 && this.lifetime >= this.maxLifetime;
    }

    public void setVelocity(Vec3 vel) {
        this.setDeltaMovement(vel);
    }

    public Vec3 getVelocity() {
        return this.getDeltaMovement();
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public int getSpawnDelayTicks() {
        return this.spawnDelay;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.owner != null && !this.owner.isAlive()) {
            this.discard();
            return;
        }

        if (this.spawnDelay > 0) {
            this.spawnDelay = this.spawnDelay - 1;
            return;
        }

        this.lifetime = this.lifetime + 1;
        if (this.isExpired()) {
            this.discard();
            return;
        }

        Vec3 v = this.getDeltaMovement();
        if (v.lengthSqr() > 0.000001D) {
            this.move(MoverType.SELF, v);
        }

        onSpiritTick();
    }

    protected abstract void onSpiritTick();

    protected void playSpiritSpawnSound() {
    }

    protected void playSpiritHitSound() {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
        this.maxLifetime = tag.getInt("MaxLifetime");
        this.spawnDelay = tag.getInt("SpawnDelay");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
        tag.putInt("MaxLifetime", this.maxLifetime);
        tag.putInt("SpawnDelay", this.spawnDelay);
    }
}
