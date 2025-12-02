package com.pgalaxyp.fragmento.NEW;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class NewAbstractProjectile extends Projectile {

    private static final EntityDataAccessor<Boolean> CHARGED_STATE =
            SynchedEntityData.defineId(NewAbstractProjectile.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SPAWN_DELAY_TICKS =
            SynchedEntityData.defineId(NewAbstractProjectile.class, EntityDataSerializers.INT);

    private static final int HITS_REQUIRED_FOR_CHARGED_STATE = 4;

    protected NewAbstractProjectile(EntityType<? extends NewAbstractProjectile> type, Level level) {
        super(type, level);
    }

    protected NewAbstractProjectile(EntityType<? extends NewAbstractProjectile> type, Level level,
                                    LivingEntity owner, boolean startCharged) {
        this(type, level);
        this.setOwner(owner);
        this.setChargedState(startCharged);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CHARGED_STATE, false);
        builder.define(SPAWN_DELAY_TICKS, 0);
    }

    public void setChargedState(boolean charged) {
        this.entityData.set(CHARGED_STATE, charged);
    }

    public boolean isInChargedState() {
        return this.entityData.get(CHARGED_STATE);
    }

    public static int getHitsRequiredForChargedState() {
        return HITS_REQUIRED_FOR_CHARGED_STATE;
    }

    public void setSpawnDelayTicks(int ticks) {
        this.entityData.set(SPAWN_DELAY_TICKS, ticks);
    }

    public int getSpawnDelayTicks() {
        return this.entityData.get(SPAWN_DELAY_TICKS);
    }

    @Override
    public void tick() {
        int delay = this.getSpawnDelayTicks();
        if (delay > 0) {
            this.entityData.set(SPAWN_DELAY_TICKS, delay - 1);
            super.tick();
            return;
        }

        if (!this.level().isClientSide) {
            HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hit.getType() != HitResult.Type.MISS) {
                this.onHit(hit);
            }
        }

        if (!this.isRemoved()) {
            Vec3 mov = this.getDeltaMovement();
            this.setPos(this.getX() + mov.x, this.getY() + mov.y, this.getZ() + mov.z);
        }

        if (this.tickCount > getMaxLifetimeInTicks()) {
            this.discard();
        }

        super.tick();
    }

    protected int getMaxLifetimeInTicks() {
        return 30;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (this.level().isClientSide) return;

        if (result.getType() == HitResult.Type.ENTITY) {
            handleEntityHitDamageAndEffects((EntityHitResult) result);
        }

        if (!this.isInChargedState()) {
            this.discard();
        }
    }

    protected boolean shouldDealDamage(LivingEntity target) {
        return true;
    }

    protected void handleEntityHitDamageAndEffects(EntityHitResult hitResult) {
        Entity hit = hitResult.getEntity();
        Entity owner = this.getOwner();

        if (hit instanceof LivingEntity living && living.isAlive()) {

            Vec3 original = living.getDeltaMovement();

            if (this.shouldDealDamage(living)) {
                float damage = this.isInChargedState()
                        ? this.getChargedDamageAmount()
                        : this.getNormalDamageAmount();

                DamageSource source = this.damageSources().thrown(this, owner);
                living.hurt(source, damage);
            }

            living.setDeltaMovement(original);

            if (this.isInChargedState()) applyChargedHitEffects(living);
            else applyNormalHitEffects(living);
        }
    }

    protected float getNormalDamageAmount() {
        return 3.0F;
    }

    protected float getChargedDamageAmount() {
        return 5.0F;
    }

    protected void applyNormalHitEffects(LivingEntity target) {}
    protected void applyChargedHitEffects(LivingEntity target) {}
}
