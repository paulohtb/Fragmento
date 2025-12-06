package com.pgalaxyp.fragmento.feature.bard.common.spirit;

import com.pgalaxyp.fragmento.feature.bard.common.combat.BardWeaponProfile;
import com.pgalaxyp.fragmento.feature.bard.common.data.AbilityChargeData;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.InstrumentBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BardSpirit extends SpiritTargetBase implements GeoEntity {

    private boolean charged;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation SPAWN_ANIM =
            RawAnimation.begin().thenPlayAndHold("spawn");
    private static final RawAnimation DESPAWN_ANIM =
            RawAnimation.begin().thenPlayAndHold("despawn");

    protected BardSpirit(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    public void setCharged(boolean value) {
        this.charged = value;
    }

    public boolean isCharged() {
        return this.charged;
    }

    protected abstract BardWeaponProfile getProfile();

    public SpiritAnimationPhase getAnimationPhase() {
        BardWeaponProfile profile = this.getProfile();
        boolean isCharged = this.charged;

        int spawnTicks = profile.getIdleTicks(isCharged);
        int age = this.getLifetime();

        if (age < spawnTicks) {
            return SpiritAnimationPhase.SPAWN;
        }

        if (this.hasHit()) {
            return SpiritAnimationPhase.DESPAWN;
        }

        return SpiritAnimationPhase.ACTIVE;
    }

    @Override
    protected void onTargetHit(LivingEntity target) {
        BardWeaponProfile profile = this.getProfile();
        boolean isCharged = this.charged;

        LivingEntity owner = this.getOwner();
        if (!isCharged && owner instanceof ServerPlayer serverPlayer) {
            ItemStack stack = serverPlayer.getMainHandItem();
            if (stack.getItem() instanceof InstrumentBase) {
                AbilityChargeData.incrementCharge(stack);
            }
        }

        float damage = profile.getDamage(isCharged);
        target.hurt(target.damageSources().magic(), damage);
        target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.0D, 0.0D));
        target.hasImpulse = true;

        if (isCharged) {
            this.applyChargedHitEffects(target);
        } else {
            this.applyBasicHitEffects(target);
        }

        this.playHitSound(target);
    }

    protected void applyBasicHitEffects(LivingEntity target) {
    }

    protected void applyChargedHitEffects(LivingEntity target) {
    }

    protected void playHitSound(LivingEntity target) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.charged = tag.getBoolean("Charged");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Charged", this.charged);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(
                this,
                "bard_spirit_controller",
                0,
                this::animationPredicate
        ));
    }

    private <E extends BardSpirit> PlayState animationPredicate(AnimationState<E> state) {
        SpiritAnimationPhase phase = this.getAnimationPhase();

        if (phase == SpiritAnimationPhase.DESPAWN) {
            state.setAndContinue(DESPAWN_ANIM);
        } else {
            state.setAndContinue(SPAWN_ANIM);
        }

        return PlayState.CONTINUE;
    }
}
