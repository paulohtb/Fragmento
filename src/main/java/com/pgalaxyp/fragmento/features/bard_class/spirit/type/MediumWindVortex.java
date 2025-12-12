package com.pgalaxyp.fragmento.features.bard_class.spirit.type;

import com.pgalaxyp.fragmento.features.bard_class.spirit.controller.SpiritConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import java.util.UUID;

public class MediumWindVortex extends Entity implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation FULL = RawAnimation.begin().thenLoop("vortex");

    private int lifetime;
    private LivingEntity owner;
    private UUID ownerUuid;

    public MediumWindVortex(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.lifetime = 0;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner != null ? owner.getUUID() : null;
    }

    private LivingEntity getOwnerResolved() {
        if (owner != null && owner.isAlive()) {
            return owner;
        }

        if (ownerUuid != null && level() instanceof ServerLevel sl) {
            ServerPlayer p = (ServerPlayer) sl.getPlayerByUUID(ownerUuid);
            if (p != null && p.isAlive()) {
                owner = p;
                return owner;
            }
        }

        owner = null;
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
        if (tag.hasUUID("OwnerUUID")) {
            this.ownerUuid = tag.getUUID("OwnerUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
        if (ownerUuid != null) {
            tag.putUUID("OwnerUUID", ownerUuid);
        }
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        if (this.level().isClientSide()) return;

        // Lifetime final
        if (lifetime >= 81) {
            discard();
            return;
        }

        LivingEntity owner = getOwnerResolved();

        double radius = SpiritConstants.VORTEX_RADIUS;
        Vec3 center = position();

        AABB area = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );

        // Agora inclui owner e players normalmente
        List<LivingEntity> list = level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                LivingEntity::isAlive
        );

        // Sucção (owner é excluído)
        for (LivingEntity e : list) {
            if (owner != null && e.getUUID().equals(owner.getUUID())) {
                continue;
            }

            Vec3 dir = center.subtract(e.position());
            double len = dir.lengthSqr();
            if (len < 1.0E-4) continue;

            Vec3 pull = dir.normalize().scale(SpiritConstants.VORTEX_PULL_STRENGTH);
            e.setDeltaMovement(e.getDeltaMovement().add(pull));
            e.hurtMarked = true;
        }

        // Impulso vertical a cada 40 ticks
        if (lifetime % 40 == 0) {
            applyPeriodicImpulse(list, owner);
        }

        // TODOS recebem glowing (inclui owner)
        for (LivingEntity e : list) {
            e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
        }
    }

    private void applyPeriodicImpulse(List<LivingEntity> list, LivingEntity owner) {
        for (LivingEntity e : list) {
            if (owner != null && e.getUUID().equals(owner.getUUID())) {
                continue;
            }

            e.setDeltaMovement(
                    e.getDeltaMovement().x,
                    1.0,
                    e.getDeltaMovement().z
            );
            e.hurtMarked = true;
        }
    }

    private <E extends MediumWindVortex> PlayState predicate(AnimationState<E> state) {
        state.getController().setAnimation(FULL);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "vortex_ctrl", 0, this::predicate));
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
}
