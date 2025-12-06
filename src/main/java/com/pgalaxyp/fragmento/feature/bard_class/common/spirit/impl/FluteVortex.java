package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.feature.bard_class.common.config.FluteConstants;
import org.slf4j.Logger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class FluteVortex extends Entity implements GeoEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation VORTEX_ANIM = RawAnimation.begin().thenLoop("vortex");

    private int lifetime;
    private LivingEntity owner;

    public FluteVortex(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
    }

    @Override
    public void tick() {
        super.tick();
        this.lifetime++;

        if (this.level().isClientSide()) {
            return;
        }

        if (this.lifetime > FluteConstants.VORTEX_LIFETIME_TICKS) {
            this.discard();
            return;
        }

        Vec3 center = this.position();
        double radius = FluteConstants.VORTEX_RADIUS;
        AABB area = new AABB(
                center.x - radius,
                center.y - 1.0,
                center.z - radius,
                center.x + radius,
                center.y + 2.0,
                center.z + radius
        );

        List<LivingEntity> entities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive() && e != this.owner
        );

        for (LivingEntity e : entities) {
            Vec3 dir = center.subtract(e.position());
            double distSq = dir.lengthSqr();
            if (distSq < 1.0e-4) {
                continue;
            }
            Vec3 pull = dir.normalize().scale(FluteConstants.VORTEX_PULL_STRENGTH);
            e.setDeltaMovement(e.getDeltaMovement().add(pull));
            e.hurtMarked = true;
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "vortex_ctrl", 0, this::predicate));
    }

    private <E extends FluteVortex> PlayState predicate(AnimationState<E> state) {
        state.setAndContinue(VORTEX_ANIM);
        return PlayState.CONTINUE;
    }
}
