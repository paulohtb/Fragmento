package com.pgalaxyp.fragmento.entity.lira_projectile;

import com.pgalaxyp.fragmento.entity.AbstractProjectile;
import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LiraProjectile extends AbstractProjectile implements GeoEntity {

    private static final EntityDataAccessor<Boolean> IS_CHARGED = SynchedEntityData.defineId(LiraProjectile.class, EntityDataSerializers.BOOLEAN);
    protected void defineSynchedData(SynchedEntityData.Builder builder) { builder.define(IS_CHARGED, false); }

    public void setCharged(boolean charged) { this.entityData.set(IS_CHARGED, charged); }
    public boolean isCharged() { return this.entityData.get(IS_CHARGED); }

    public LiraProjectile(EntityType<? extends Projectile> type, Level world) { super(type, world); }

    public LiraProjectile(Level world, LivingEntity shooter, boolean charged) {
        this(EntitiesRegistry.LIRA_PROJECTILE.get(), world);
        setOwner(shooter);
        setCharged(charged);
        shoot(shooter.getLookAngle());
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 30) discard();
    }

    protected boolean shouldDiscardAfterHit() { return !isCharged(); }

    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.discard();
            return;
        }

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity hit = ((EntityHitResult) hitResult).getEntity();
            Entity owner = this.getOwner();

            if (hit instanceof Mob mob) {
                DamageSource source = owner != null
                        ? this.damageSources().source(DamageTypes.GENERIC)
                        : this.damageSources().generic();

                if (mob instanceof Enemy) {
                    if (mob.hurt(source, 1.0F)) {
                        mob.invulnerableTime = 0;
                    }
                } else {
                    mob.heal(isCharged() ? 20F : 5F);
                }
            }
        }

        if (!isCharged()) {
            this.discard();
        }
    }

    private final AnimatableInstanceCache ANIMATION_CACHE = GeckoLibUtil.createInstanceCache(this);
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.ANIMATION_CACHE; }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}