package com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile;

import com.pgalaxyp.fragmento.entity.bard.projectiles.AbstractProjectile;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FluteProjectile extends AbstractProjectile implements GeoEntity {

    private static final EntityDataAccessor<Boolean> IS_CHARGED =
            SynchedEntityData.defineId(FluteProjectile.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_CHARGED, false);
    }

    public void setCharged(boolean charged) { this.entityData.set(IS_CHARGED, charged); }
    public boolean isCharged() { return this.entityData.get(IS_CHARGED); }

    public FluteProjectile(EntityType<? extends Projectile> type, Level world) {
        super(type, world);
    }

    public FluteProjectile(Level world, LivingEntity shooter, boolean charged) {
        this(EntitiesRegistry.FLUTE_PROJECTILE.get(), world);
        setOwner(shooter);
        setCharged(charged);
        Vec3 look = shooter.getLookAngle();
        shoot(look.x, look.y, look.z, 2.0F, 0.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 30) this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected boolean shouldDiscardAfterHit() {
        return !isCharged();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) result;
            Entity hit = entityHit.getEntity();
            Entity owner = getOwner();

            if (hit instanceof LivingEntity living && living.isAlive()) {
                DamageSource source = this.damageSources().indirectMagic(this, owner);
                living.hurt(source, 1.0F);

                if (isCharged()) {
                    living.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            EffectsRegistry.CHARMED, 100, 0));
                }
            }
        }
        if (!isCharged()) this.remove(RemovalReason.DISCARDED);
    }

    private final AnimatableInstanceCache ANIMATION_CACHE = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ANIMATION_CACHE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0, state -> PlayState.STOP));
    }
}