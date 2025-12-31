package com.pgalaxyp.fragmento.combat.engine.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class FluteSpiritEntity extends Entity {

    private UUID ownerId;
    private int targetId;
    private float damage;
    private int lifeTicks;
    private int maxLifeTicks;

    public FluteSpiritEntity(EntityType<? extends FluteSpiritEntity> type, Level level) {
        super(type, level);
        this.damage = 4.0f;
        this.lifeTicks = 0;
        this.maxLifeTicks = 10;
        this.targetId = 0;
    }

    public void configure(UUID ownerIdValue, int targetIdValue, float damageValue, int maxLifeTicksValue) {
        this.ownerId = ownerIdValue;
        this.targetId = Math.max(0, targetIdValue);
        this.damage = Math.max(0.0f, damageValue);
        this.maxLifeTicks = Math.max(1, maxLifeTicksValue);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        lifeTicks = lifeTicks + 1;

        if (lifeTicks == 1) {
            applyHitOnce(serverLevel);
        }

        if (lifeTicks >= maxLifeTicks) {
            discard();
        }
    }

    private void applyHitOnce(ServerLevel serverLevel) {
        if (ownerId == null) {
            return;
        }

        Entity ownerEntity = serverLevel.getPlayerByUUID(ownerId);
        if (!(ownerEntity instanceof LivingEntity owner)) {
            return;
        }

        if (targetId <= 0) {
            return;
        }

        Entity targetEntity = serverLevel.getEntity(targetId);
        if (!(targetEntity instanceof LivingEntity target)) {
            return;
        }
        if (!target.isAlive()) {
            return;
        }

        DamageSource src = serverLevel.damageSources().mobAttack(owner);
        target.hurt(src, damage);
    }
}