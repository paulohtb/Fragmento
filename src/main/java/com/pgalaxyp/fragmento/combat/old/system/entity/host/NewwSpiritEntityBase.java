package com.pgalaxyp.fragmento.combat.old.system.entity.host;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.combat.old.core.util.MathUtil;
import com.pgalaxyp.fragmento.combat.old.system.entity.event.SpiritEventSource;
import com.pgalaxyp.fragmento.combat.old.system.entity.event.SpiritSelf;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public abstract class NewwSpiritEntityBase extends SkillEntityBase implements SpiritSelf, SpiritEventSource {

    public static final EntityDataAccessor<Integer> LIFETIME =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Boolean> CASTED =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Byte> ANIM_KEY =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.BYTE);

    public static final EntityDataAccessor<Byte> VISUAL_KEY =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.BYTE);

    public static final EntityDataAccessor<Integer> VISUAL_START_LIFETIME =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> VISUAL_DURATION =
            SynchedEntityData.defineId(NewwSpiritEntityBase.class, EntityDataSerializers.INT);

    public static final byte VISUAL_NONE = 0;
    public static final byte VISUAL_HOVER = 1;
    public static final byte VISUAL_BURST = 2;

    private int lifetimeTicks;

    private UUID ownerUuid;
    private int targetEntityId;
    private UUID sourceInstrumentUuid;

    private net.minecraft.world.entity.LivingEntity cachedOwner;
    private net.minecraft.world.entity.LivingEntity cachedTarget;

    private boolean pendingCasted;
    private boolean pendingCancelled;

    private int despawnTicksRemaining;

    private boolean controllerInitialized;

    protected NewwSpiritEntityBase(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected final void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFETIME, 0);
        builder.define(CASTED, false);
        builder.define(ANIM_KEY, (byte) 0);

        builder.define(VISUAL_KEY, VISUAL_NONE);
        builder.define(VISUAL_START_LIFETIME, 0);
        builder.define(VISUAL_DURATION, 1);
    }

    @Override
    protected final void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected final void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public final boolean shouldBeSaved() {
        return false;
    }

    @Override
    public final boolean isPickable() {
        return false;
    }

    @Override
    public final boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public final boolean isPushable() {
        return false;
    }

    public final int summon(
            net.minecraft.world.entity.LivingEntity owner,
            net.minecraft.world.entity.LivingEntity target,
            ServerLevel level,
            SkillMode mode,
            ItemStack sourceItem
    ) {
        if (level == null) return 0;

        lifetimeTicks = 0;
        setLifetimeSynced(0);

        pendingCasted = false;
        pendingCancelled = false;

        despawnTicksRemaining = 0;
        setCastedSynced(false);

        setVisualState(VISUAL_NONE, 0, 1);

        ownerUuid = owner != null ? owner.getUUID() : null;
        targetEntityId = target != null ? target.getId() : 0;
        sourceInstrumentUuid = resolveSourceInstrumentUuid(sourceItem);

        cachedOwner = null;
        cachedTarget = null;

        onSummoned(mode);

        if (owner != null) {
            Vec3 spawn = resolveSpawnPosition(owner, target, level, mode);
            if (spawn == null) {
                spawn = owner.getEyePosition();
            }

            setPos(spawn.x, spawn.y, spawn.z);
            setYRot(owner.getYRot());
            setXRot(owner.getXRot());
            yRotO = getYRot();
            xRotO = getXRot();
        }

        level.addFreshEntity(this);
        return getId();
    }

    protected Vec3 resolveSpawnPosition(
            net.minecraft.world.entity.LivingEntity owner,
            net.minecraft.world.entity.LivingEntity target,
            ServerLevel level,
            SkillMode mode
    ) {
        return owner != null ? owner.getEyePosition() : null;
    }

    protected abstract void onSummoned(SkillMode mode);

    @Override
    protected final void serverPreControllers() {
        lifetimeTicks++;
        setLifetimeSynced(lifetimeTicks);

        if (despawnTicksRemaining > 0) {
            despawnTicksRemaining = (int) (despawnTicksRemaining + MathUtil.negate(1));
            if (despawnTicksRemaining <= 0) {
                remove(RemovalReason.DISCARDED);
                return;
            }
        }

        if (level() instanceof ServerLevel sl) {
            cachedOwner = com.pgalaxyp.fragmento.combat.old.system.entity.resolve.SpiritResolve.resolveOwner(sl, ownerUuid, cachedOwner);
            cachedTarget = com.pgalaxyp.fragmento.combat.old.system.entity.resolve.SpiritResolve.resolveTarget(sl, targetEntityId, cachedTarget);
        }
    }

    @Override
    protected void clientTick() {
    }

    public final net.minecraft.world.entity.LivingEntity getOwner() {
        return cachedOwner;
    }

    public final net.minecraft.world.entity.LivingEntity getTarget() {
        return cachedTarget;
    }

    public final UUID getOwnerUuid() {
        return ownerUuid;
    }

    public final int getTargetEntityId() {
        return targetEntityId;
    }

    public final UUID getSourceInstrumentUuid() {
        return sourceInstrumentUuid;
    }

    public final int getLifetime() {
        return entityData.get(LIFETIME);
    }

    public final boolean isCasted() {
        return entityData.get(CASTED);
    }

    @Override
    public final void setAnimKey(byte key) {
        if (entityData.get(ANIM_KEY) != key) {
            entityData.set(ANIM_KEY, key);
        }
    }

    public final byte getAnimKey() {
        return entityData.get(ANIM_KEY);
    }

    public final byte getVisualKey() {
        return entityData.get(VISUAL_KEY);
    }

    public final int getVisualStartLifetime() {
        return entityData.get(VISUAL_START_LIFETIME);
    }

    public final int getVisualDuration() {
        return entityData.get(VISUAL_DURATION);
    }

    public final void setVisualState(byte key, int startLifetime, int durationTicks) {
        if (level().isClientSide()) return;

        byte k = key;
        int s = Math.max(0, startLifetime);
        int d = Math.max(1, durationTicks);

        if (entityData.get(VISUAL_KEY) != k) entityData.set(VISUAL_KEY, k);
        if (entityData.get(VISUAL_START_LIFETIME) != s) entityData.set(VISUAL_START_LIFETIME, s);
        if (entityData.get(VISUAL_DURATION) != d) entityData.set(VISUAL_DURATION, d);
    }

    @Override
    public final void markCasted() {
        if (level().isClientSide()) return;

        if (!entityData.get(CASTED)) {
            setCastedSynced(true);
        }
        pendingCasted = true;
    }

    @Override
    public final void markCancelled() {
        if (level().isClientSide()) return;
        pendingCancelled = true;
    }

    @Override
    public final boolean consumeCasted() {
        if (!pendingCasted) return false;
        pendingCasted = false;
        return true;
    }

    @Override
    public final boolean consumeCancelled() {
        if (!pendingCancelled) return false;
        pendingCancelled = false;
        return true;
    }

    @Override
    public final void requestDespawn() {
        requestDespawn(0);
    }

    @Override
    public final void requestDespawn(int delayTicks) {
        if (level().isClientSide()) return;

        int d = Math.max(0, delayTicks);
        if (despawnTicksRemaining == 0 || d < despawnTicksRemaining) {
            despawnTicksRemaining = d;
        }

        pendingCancelled = true;

        if (despawnTicksRemaining == 0) {
            remove(RemovalReason.DISCARDED);
        }
    }

    public final void moveServer(Vec3 delta) {
        if (level().isClientSide()) return;
        if (delta == null) return;
        if (delta.lengthSqr() <= 0.000000000001) return;
        setDeltaMovement(delta);
    }

    public final void clearTarget() {
        if (level().isClientSide()) return;
        targetEntityId = 0;
        cachedTarget = null;
    }

    protected final void ensureControllerRegistered(com.pgalaxyp.fragmento.combat.old.system.entity.controller.EntityController<?> controller) {
        if (controllerInitialized) return;
        controllers.add(controller);
        controllerInitialized = true;
    }

    protected final void resetControllers() {
        controllers.clear();
        controllerInitialized = false;
    }

    private void setLifetimeSynced(int value) {
        if (entityData.get(LIFETIME) != value) {
            entityData.set(LIFETIME, value);
        }
    }

    private void setCastedSynced(boolean value) {
        if (entityData.get(CASTED) != value) {
            entityData.set(CASTED, value);
        }
    }

    private static UUID resolveSourceInstrumentUuid(ItemStack sourceItem) {
        if (sourceItem == null || sourceItem.isEmpty()) return null;
        return BardCatalystIdService.getOrCreate(sourceItem);
    }
}