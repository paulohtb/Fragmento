package com.pgalaxyp.fragmento.feature.bard_class.common.spirit;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.feature.bard_class.common.data.SpiritAnimationData;
import org.slf4j.Logger;
import com.pgalaxyp.fragmento.feature.bard_class.common.data.InstrumentChargeData;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller.SpiritAnimation;
import com.pgalaxyp.fragmento.feature.bard_class.common.weapon.InstrumentBase;
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

    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean charged;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private boolean forceDespawnAnimation;

    private static final RawAnimation SPAWN_ANIM = RawAnimation.begin().thenPlay("spawn");
    private static final RawAnimation SHORT_ANIM = RawAnimation.begin().thenPlay("travel_short");
    private static final RawAnimation MEDIUM_ANIM = RawAnimation.begin().thenPlay("travel_medium");
    private static final RawAnimation LONG_ANIM = RawAnimation.begin().thenPlay("travel_long");
    private static final RawAnimation DESPAWN_ANIM = RawAnimation.begin().thenPlayAndHold("despawn");

    public BardSpirit(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    private RawAnimation animFromEnum(SpiritAnimation a) {
        return switch (a) {
            case SPAWN -> SPAWN_ANIM;
            case TRAVEL_SHORT -> SHORT_ANIM;
            case TRAVEL_MEDIUM -> MEDIUM_ANIM;
            case TRAVEL_LONG -> LONG_ANIM;
            case DESPAWN -> DESPAWN_ANIM;
            default -> null;
        };
    }

    public void selectTravelAnimation(double dist) {
        LOGGER.info("[Spirit {}] selectTravelAnimation(dist={})", this.getId(), dist);
        SpiritAnimation a;
        if (dist <= 4.0) {
            a = SpiritAnimation.TRAVEL_SHORT;
            LOGGER.info("[Spirit {}] travel anim = SHORT", this.getId());
        } else if (dist <= 8.0) {
            a = SpiritAnimation.TRAVEL_MEDIUM;
            LOGGER.info("[Spirit {}] travel anim = MEDIUM", this.getId());
        } else {
            a = SpiritAnimation.TRAVEL_LONG;
            LOGGER.info("[Spirit {}] travel anim = LONG", this.getId());
        }
        this.entityData.set(SpiritAnimationData.ANIM, a.id);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar r) {
        LOGGER.info("[Spirit {}] registerControllers()", this.getId());
        r.add(new AnimationController<>(this, "spirit_ctrl", 0, this::predicate));
    }

    private <E extends BardSpirit> PlayState predicate(AnimationState<E> s) {
        LOGGER.info("[Spirit {}] predicate() lifetime={} hit={} forcedDespawn={}",
                this.getId(),
                this.getLifetime(),
                this.hasHit(),
                this.forceDespawnAnimation
        );

        if (this.forceDespawnAnimation || this.hasHit()) {
            LOGGER.info("[Spirit {}] playing DESPAWN_ANIM", this.getId());
            s.setAndContinue(DESPAWN_ANIM);
            return PlayState.CONTINUE;
        }

        if (this.getLifetime() < this.getSpawnDelayTicks()) {
            LOGGER.info("[Spirit {}] playing SPAWN_ANIM", this.getId());
            s.setAndContinue(SPAWN_ANIM);
            return PlayState.CONTINUE;
        }

        int id = this.entityData.get(SpiritAnimationData.ANIM);
        SpiritAnimation a = SpiritAnimation.fromId(id);
        RawAnimation ra = animFromEnum(a);

        if (ra != null) {
            LOGGER.info("[Spirit {}] playing TRAVEL ANIM id={}", this.getId(), id);
            s.setAndContinue(ra);
            return PlayState.CONTINUE;
        }

        LOGGER.info("[Spirit {}] predicate found NO ANIMATION", this.getId());
        return PlayState.CONTINUE;
    }

    public void setCharged(boolean v) {
        this.charged = v;
    }

    public boolean isCharged() {
        return this.charged;
    }

    public void startDespawnVisual() {
        this.forceDespawnAnimation = true;
        this.entityData.set(SpiritAnimationData.ANIM, SpiritAnimation.DESPAWN.id);
    }

    @Override
    protected void onTargetHitInternal(LivingEntity target) {
        LOGGER.info("[Spirit {}] onTargetHitInternal() target={}", this.getId(), target.getName().getString());
        LivingEntity owner = this.getOwner();
        if (!charged && owner instanceof ServerPlayer sp) {
            ItemStack st = sp.getMainHandItem();
            if (st.getItem() instanceof InstrumentBase) {
                InstrumentChargeData.incrementCharge(st);
            }
        }
        float dmg = charged ? getChargedDamage() : getBasicDamage();
        target.hurt(target.damageSources().magic(), dmg);
        if (charged) applyChargedHitEffects(target);
        else applyBasicHitEffects(target);
        playHitSound(target);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag t) {
        super.readAdditionalSaveData(t);
        charged = t.getBoolean("Charged");
        forceDespawnAnimation = t.getBoolean("ForceDespawnAnim");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag t) {
        super.addAdditionalSaveData(t);
        t.putBoolean("Charged", charged);
        t.putBoolean("ForceDespawnAnim", forceDespawnAnimation);
    }

    protected abstract float getBasicDamage();
    protected abstract float getChargedDamage();
    protected void applyBasicHitEffects(LivingEntity t) {}
    protected void applyChargedHitEffects(LivingEntity t) {}
    protected void playHitSound(LivingEntity t) {}
}
