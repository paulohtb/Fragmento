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

    private static final int HITS_REQUIRED_FOR_CHARGED_STATE = 3;

    protected NewAbstractProjectile(EntityType<? extends NewAbstractProjectile> type, Level level) {
        super(type, level);
    }

    protected NewAbstractProjectile(EntityType<? extends NewAbstractProjectile> type, Level level, LivingEntity owner, boolean startCharged) {
        this(type, level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.setChargedState(startCharged);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CHARGED_STATE, false);
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

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
            }
        }

        if (!this.isRemoved()) {
            Vec3 movement = this.getDeltaMovement();
            this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
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

        if (this.level().isClientSide) {
            return;
        }

        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) result;
            this.handleEntityHitDamageAndEffects(entityHitResult);
        }

        if (!this.isInChargedState()) {
            this.discard();
        }
    }

    protected boolean shouldDealDamage(LivingEntity target) {
        return true;
    }

    protected void handleEntityHitDamageAndEffects(EntityHitResult entityHitResult) {
        Entity hit = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        if (hit instanceof LivingEntity living && living.isAlive()) {

            Vec3 originalMotion = living.getDeltaMovement();

            if (this.shouldDealDamage(living)) {
                float damage = this.isInChargedState() ? this.getChargedDamageAmount() : this.getNormalDamageAmount();
                DamageSource source = this.damageSources().thrown(this, owner);
                living.hurt(source, damage);
            }

            living.setDeltaMovement(originalMotion);

            if (this.isInChargedState()) {
                this.applyChargedHitEffects(living);
            } else {
                this.applyNormalHitEffects(living);
            }
        }
    }

    public void shootInStraightDirection(Vec3 direction, float speed, float inaccuracy) {
        Vec3 dir = direction.normalize();
        Vec3 spread = new Vec3(
                this.random.triangle(0.0, inaccuracy),
                this.random.triangle(0.0, inaccuracy),
                this.random.triangle(0.0, inaccuracy)
        );
        Vec3 finalDir = dir.add(spread).normalize().scale(speed);
        this.setDeltaMovement(finalDir);
    }

    protected float getNormalDamageAmount() {
        return 2.0F;
    }

    protected float getChargedDamageAmount() {
        return 3.0F;
    }

    protected void applyNormalHitEffects(LivingEntity target) {
    }

    protected void applyChargedHitEffects(LivingEntity target) {
    }
}
