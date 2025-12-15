package com.pgalaxyp.fragmento.content.bard.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import java.util.List;
import java.util.UUID;

public abstract class WindVortexBase extends Entity implements GeoEntity {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation FULL =
            RawAnimation.begin().thenLoop("vortex");

    private int lifetime;
    private int scanCounter;

    private LivingEntity owner;
    private UUID ownerUuid;

    private boolean reservedCount;

    protected WindVortexBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    protected abstract WindVortexLimitService.VortexTier tier();

    protected abstract int lifetimeLimitTicks();

    protected abstract double radius();

    protected abstract double pullStrength();

    protected abstract int scanIntervalTicks();

    protected abstract int maxAffectedPerScan();

    protected void applyExtraEffect(LivingEntity e) {
    }

    protected void applyPlayerBuff(Player p) {
    }

    protected boolean shouldProcessThisTick(int lifetimeTicks) {
        scanCounter++;
        int interval = scanIntervalTicks();
        if (scanCounter < interval) return false;
        scanCounter = 0;
        return true;
    }

    protected final int lifetimeTicks() {
        return lifetime;
    }

    public final void setOwner(LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner != null ? owner.getUUID() : null;
    }

    final void markReservedCount() {
        reservedCount = true;
    }

    protected LivingEntity resolveOwner() {
        if (owner != null && owner.isAlive()) return owner;
        if (ownerUuid != null && level() instanceof ServerLevel sl) {
            Player p = sl.getPlayerByUUID(ownerUuid);
            if (p != null && p.isAlive()) {
                owner = p;
                return owner;
            }
        }
        owner = null;
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            lifetime++;
            return;
        }

        lifetime++;

        int limit = lifetimeLimitTicks();

        if (lifetime > limit) {
            releaseReservationIfNeeded();
            discard();
            return;
        }

        if (shouldProcessThisTick(lifetime)) {
            doScanAndApply();
        }

        if (lifetime >= limit) {
            releaseReservationIfNeeded();
            discard();
        }
    }

    private void doScanAndApply() {
        Vec3 center = position();
        double r = radius();

        AABB area = new AABB(
                center.x - r, center.y - r, center.z - r,
                center.x + r, center.y + r, center.z + r
        );

        LivingEntity resolvedOwner = resolveOwner();
        UUID ownerId = resolvedOwner != null ? resolvedOwner.getUUID() : null;

        List<LivingEntity> entities = level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                LivingEntity::isAlive
        );

        int max = maxAffectedPerScan();
        int affected = 0;

        for (LivingEntity e : entities) {

            if (e instanceof Player p) {
                applyPlayerBuff(p);
                continue;
            }

            if (ownerId != null && ownerId.equals(e.getUUID())) continue;

            Vec3 toCenter = center.subtract(e.position());
            if (toCenter.lengthSqr() < 1.0E-6) continue;

            Vec3 force = toCenter.normalize().scale(pullStrength());
            e.setDeltaMovement(e.getDeltaMovement().add(force));
            e.hurtMarked = true;

            applyExtraEffect(e);

            if (++affected >= max) break;
        }
    }

    private void releaseReservationIfNeeded() {
        if (!reservedCount) return;
        reservedCount = false;
        if (level() instanceof ServerLevel sl) {
            WindVortexLimitService.release(sl, ownerUuid, tier());
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        releaseReservationIfNeeded();
        super.remove(reason);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new software.bernie.geckolib.animation.AnimationController<>(
                        this,
                        "vortex",
                        0,
                        state -> {
                            state.getController().setAnimation(FULL);
                            return software.bernie.geckolib.animation.PlayState.CONTINUE;
                        }
                )
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
