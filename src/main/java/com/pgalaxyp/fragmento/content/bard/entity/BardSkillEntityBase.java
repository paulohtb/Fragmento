package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.constants.BardSpiritConstants;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.core.controller.CollisionController;
import com.pgalaxyp.fragmento.core.controller.ImpulseController;
import com.pgalaxyp.fragmento.core.controller.OrientationController;
import com.pgalaxyp.fragmento.core.controller.SpawnController;
import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public abstract class BardSkillEntityBase extends SkillEntityBase {

    public static final EntityDataAccessor<Integer> LIFETIME =
            SynchedEntityData.defineId(BardSkillEntityBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> CAST_STATE =
            SynchedEntityData.defineId(BardSkillEntityBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Byte> ANIM_KEY =
            SynchedEntityData.defineId(BardSkillEntityBase.class, EntityDataSerializers.BYTE);

    public static final EntityDataAccessor<Boolean> ORIENTATION_LOCKED =
            SynchedEntityData.defineId(BardSkillEntityBase.class, EntityDataSerializers.BOOLEAN);

    protected final BardSkillState state = new BardSkillState();
    protected final BardSkillLifecycle lifecycle = new BardSkillLifecycle(this, state);

    protected SkillEntity behavior;

    public final AutoMovementController<BardSkillEntityBase> flightController;
    public final CollisionController<BardSkillEntityBase> collisionController;
    public final OrientationController<BardSkillEntityBase> orientationController;
    public final ImpulseController<BardSkillEntityBase> impulseController;
    public final SpawnController<BardSkillEntityBase> spawnController;

    private Vec3 lookAtPos;

    private int lifetimeTicks;

    private LivingEntity cachedOwner;
    private LivingEntity cachedTarget;

    protected BardSkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);

        flightController = new AutoMovementController<>(
                this,
                BardSkillEntityBase::getTarget
        );

        collisionController = new CollisionController<>(
                this,
                BardSkillEntityBase::getTarget
        );

        orientationController = new OrientationController<>(
                this,
                BardSkillEntityBase::getTarget,
                this::getLookAtPos
        );

        impulseController = new ImpulseController<>(this);

        spawnController = new SpawnController<>(
                this,
                BardSkillEntityBase::setTarget
        );

        controllers.add(spawnController);
        controllers.add(flightController);
        controllers.add(collisionController);
        controllers.add(impulseController);
        controllers.add(orientationController);
    }

    protected abstract SkillEntity createBehavior(SkillMode mode);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LIFETIME, 0);
        builder.define(CAST_STATE, BardSkillState.CastState.CHANNELING.ordinal());
        builder.define(ANIM_KEY, (byte) 0);
        builder.define(ORIENTATION_LOCKED, false);
    }

    @Override
    protected final void serverPreControllers() {
        lifetimeTicks++;
        syncInt(LIFETIME, lifetimeTicks);
        syncInt(CAST_STATE, state.castState().ordinal());

        if (behavior != null) {
            behavior.tick();
            if (isRemoved()) return;
        }

        orientationController.setEnabled(!isOrientationLocked());

        if (level() instanceof ServerLevel sl) {
            if (lifecycle.tick(sl)) {
                discard();
            }
        }
    }

    private void syncInt(EntityDataAccessor<Integer> key, int value) {
        if (entityData.get(key) != value) {
            entityData.set(key, value);
        }
    }

    public final int getLifetime() {
        return lifetimeTicks;
    }

    public final UUID getOwnerUuid() {
        return state.ownerUuid();
    }

    public final UUID getSourceInstrumentUuid() {
        return state.sourceInstrumentUuid();
    }

    public final LivingEntity getOwner() {
        UUID id = state.ownerUuid();
        if (id == null) {
            cachedOwner = null;
            return null;
        }

        if (cachedOwner != null && cachedOwner.isAlive() && id.equals(cachedOwner.getUUID())) {
            return cachedOwner;
        }

        if (!(level() instanceof ServerLevel sl)) {
            cachedOwner = null;
            return null;
        }

        Player p = sl.getPlayerByUUID(id);
        if (p != null && p.isAlive()) {
            cachedOwner = p;
            return cachedOwner;
        }

        cachedOwner = null;
        return null;
    }

    public final void setOwner(LivingEntity owner) {
        cachedOwner = owner;
        state.setOwnerUuid(owner != null ? owner.getUUID() : null);
    }

    public final LivingEntity getTarget() {
        int id = state.targetEntityId();
        if (id <= 0) {
            cachedTarget = null;
            return null;
        }

        if (cachedTarget != null && cachedTarget.isAlive() && cachedTarget.getId() == id) {
            return cachedTarget;
        }

        if (!(level() instanceof ServerLevel sl)) {
            cachedTarget = null;
            return null;
        }

        Entity e = sl.getEntity(id);
        if (e instanceof LivingEntity living && living.isAlive()) {
            cachedTarget = living;
            return cachedTarget;
        }

        cachedTarget = null;
        return null;
    }

    public final void setTarget(LivingEntity target) {
        cachedTarget = target;
        state.setTargetEntityId(target != null ? target.getId() : 0);
    }

    public final Vec3 resolveAnchorPosition() {
        Vec3 a = state.castAnchorPos();
        return a != null ? a : position();
    }

    public final boolean isCasted() {
        return lifecycle.isCasted();
    }

    public final void markCasted() {
        if (level().isClientSide()) return;
        lifecycle.markCasted();
        syncInt(CAST_STATE, state.castState().ordinal());
    }

    public final void requestDespawn() {
        requestDespawn(BardSpiritConstants.CASTED_SPIRIT_DESPAWN_DELAY_TICKS);
    }

    public final void requestDespawn(int delayTicks) {
        if (level().isClientSide()) return;
        lifecycle.requestDespawn(Math.max(0, delayTicks));
        syncInt(CAST_STATE, state.castState().ordinal());
    }

    final void onCastedInternal() {
        if (behavior != null) {
            behavior.onCasted();
        }
    }

    final void onCancelledInternal() {
        if (behavior != null) {
            behavior.onCancelled();
        }
    }

    public final int summon(
            LivingEntity caster,
            LivingEntity target,
            ServerLevel level,
            SkillMode mode,
            ItemStack sourceStack
    ) {
        if (caster == null || level == null || mode == null) return 0;

        setOwner(caster);
        setTarget(target);

        UUID sourceId = null;
        if (sourceStack != null && !sourceStack.isEmpty()) {
            sourceId = BardCatalystIdService.getOrCreate(sourceStack);
        }
        state.setSourceInstrumentUuid(sourceId);

        lifetimeTicks = 0;
        syncInt(LIFETIME, 0);

        if (mode == SkillMode.SPECIAL) {
            state.setCastState(BardSkillState.CastState.CHANNELING);
            state.setCastAnchorPos(null);
        } else {
            state.setCastState(BardSkillState.CastState.CASTED);
            Vec3 anchor = target != null ? target.position() : caster.position();
            state.setCastAnchorPos(anchor);
        }
        syncInt(CAST_STATE, state.castState().ordinal());

        behavior = createBehavior(mode);
        if (behavior == null) {
            return 0;
        }

        spawnController.applyInitialPlacement(
                caster,
                target,
                level
        );

        if (!level.addFreshEntity(this)) {
            return 0;
        }

        return getId();
    }

    public final void setAnimKey(byte key) {
        if (entityData.get(ANIM_KEY) != key) {
            entityData.set(ANIM_KEY, key);
        }
    }

    public final byte getAnimKey() {
        return entityData.get(ANIM_KEY);
    }

    public final boolean isOrientationLocked() {
        return entityData.get(ORIENTATION_LOCKED);
    }

    public final void setOrientationLocked(boolean locked) {
        if (entityData.get(ORIENTATION_LOCKED) != locked) {
            entityData.set(ORIENTATION_LOCKED, locked);
        }
    }

    public final void setLookAtPos(Vec3 pos) {
        lookAtPos = pos;
    }

    public final void clearLookAtPos() {
        lookAtPos = null;
    }

    public final Vec3 getLookAtPos() {
        return lookAtPos;
    }

    @Override
    public void push(Entity entity) {
    }
}
