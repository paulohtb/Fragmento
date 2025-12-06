package com.pgalaxyp.fragmento.feature.bard_class.common.spirit;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller.SpiritController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class SpiritBase extends Entity {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final EntityDataAccessor<Integer> ANIM =
            SynchedEntityData.defineId(SpiritBase.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(SpiritBase.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> SPAWN_DELAY =
            SynchedEntityData.defineId(SpiritBase.class, EntityDataSerializers.INT);

    private LivingEntity owner;
    private int lifetime;
    private int maxLifetime;

    private final List<SpiritController<?>> controllers = new ArrayList<>();

    protected SpiritBase(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    protected void addController(SpiritController<?> controller) {
        this.controllers.add(controller);
    }

    public void setOwner(LivingEntity entity) {
        LOGGER.info("[Spirit {}] setOwner {}", this.getId(), entity.getName().getString());
        this.owner = entity;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setMaxLifetime(int ticks) {
        LOGGER.info("[Spirit {}] setMaxLifetime = {}", this.getId(), ticks);
        this.maxLifetime = ticks;
    }

    public int getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setSpawnDelay(int ticks) {
        LOGGER.info("[Spirit {}] setSpawnDelay = {}", this.getId(), ticks);
        this.entityData.set(SPAWN_DELAY, ticks);
    }

    public int getSpawnDelayTicks() {
        return this.entityData.get(SPAWN_DELAY);
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void setVelocity(Vec3 vel) {
        LOGGER.info("[Spirit {}] setVelocity {}", this.getId(), vel);
        this.setDeltaMovement(vel);
    }

    public Vec3 getVelocity() {
        return this.getDeltaMovement();
    }

    @Override
    public void tick() {
        super.tick();

        LOGGER.info("[Spirit {}] tick lifetime={} spawnDelay={}", this.getId(), lifetime, getSpawnDelayTicks());

        if (this.owner != null && !this.owner.isAlive()) {
            LOGGER.info("[Spirit {}] owner dead, discarding", this.getId());
            this.discard();
            return;
        }

        this.lifetime++;

        Vec3 v = this.getDeltaMovement();
        if (v.lengthSqr() > 0.000001D) {
            LOGGER.info("[Spirit {}] moving {}", this.getId(), v);
            this.move(MoverType.SELF, v);
        }

        if (!this.level().isClientSide()) {
            for (SpiritController<?> controller : this.controllers) {
                controller.tick();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ANIM, 0);
        builder.define(HIT, false);
        builder.define(SPAWN_DELAY, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
        this.maxLifetime = tag.getInt("MaxLifetime");
        this.entityData.set(ANIM, tag.getInt("Anim"));
        this.entityData.set(HIT, tag.getBoolean("Hit"));
        this.entityData.set(SPAWN_DELAY, tag.getInt("SpawnDelay"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
        tag.putInt("MaxLifetime", this.maxLifetime);
        tag.putInt("Anim", this.entityData.get(ANIM));
        tag.putBoolean("Hit", this.entityData.get(HIT));
        tag.putInt("SpawnDelay", this.entityData.get(SPAWN_DELAY));
    }
}
