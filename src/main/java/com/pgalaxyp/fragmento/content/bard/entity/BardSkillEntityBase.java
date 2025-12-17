package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardSpiritConstants;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.core.controller.CollisionController;
import com.pgalaxyp.fragmento.core.controller.ImpulseController;
import com.pgalaxyp.fragmento.core.controller.OrientationController;
import com.pgalaxyp.fragmento.core.controller.SpawnController;
import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import java.util.UUID;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    private LivingEntity cachedTarget;

    protected BardSkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);

        this.noPhysics = true;
        this.setNoGravity(true);

        flightController = new AutoMovementController<>(this, BardSkillEntityBase::getTarget);
        collisionController = new CollisionController<>(this, BardSkillEntityBase::getTarget);
        orientationController =
                new OrientationController<>(this, BardSkillEntityBase::getTarget, this::getLookAtPos);
        impulseController = new ImpulseController<>(this);
        spawnController = new SpawnController<>(this, BardSkillEntityBase::setTarget);

        controllers.add(spawnController);
        controllers.add(flightController);
        controllers.add(impulseController);
        controllers.add(collisionController);
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
    protected void serverPreControllers() {
        lifetimeTicks++;
        syncInt(LIFETIME, lifetimeTicks);
        syncInt(CAST_STATE, state.castState().ordinal());
        orientationController.setEnabled(!isOrientationLocked());
    }

    @Override
    protected void serverPostControllers() {
        if (behavior != null) {
            behavior.tick();
            if (isRemoved()) {
                return;
            }
        }

        if (level() instanceof ServerLevel sl) {
            if (lifecycle.tick(sl)) {
                discard();
            }
        }
    }

    @Override
    public int summon(
            LivingEntity owner,
            LivingEntity target,
            ServerLevel level,
            SkillMode mode,
            ItemStack sourceItem
    ) {
        if (level == null) return 0;

        state.setOwnerUuid(owner != null ? owner.getUUID() : null);
        state.setTargetEntityId(target != null ? target.getId() : 0);
        state.setSourceInstrumentUuid(
                BardCatalystIdService.getOrCreate(sourceItem)
        );

        spawnController.applyInitialPlacement(owner, target, level);

        behavior = createBehavior(mode);

        if (!level.addFreshEntity(this)) return 0;

        return getId();
    }

    private void syncInt(EntityDataAccessor<Integer> key, int value) {
        if (entityData.get(key) != value) {
            entityData.set(key, value);
        }
    }

    public final int getLifetime() {
        return lifetimeTicks;
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
    public void onCastedInternal() {
        if (behavior != null) {
            behavior.onCasted();
        }
    }

    @Override
    public void onCancelledInternal() {
        if (behavior != null) {
            behavior.onCancelled();
        }
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public LivingEntity getOwner() {
        UUID id = state.ownerUuid();
        if (id == null) return null;

        if (level() instanceof ServerLevel sl) {
            Entity e = sl.getEntity(id);
            if (e instanceof LivingEntity l && l.isAlive()) return l;
        }

        return null;
    }

    @Override
    public UUID getOwnerUuid() {
        return state.ownerUuid();
    }

    @Override
    public UUID getSourceInstrumentUuid() {
        return state.sourceInstrumentUuid();
    }

    @Override
    public Vec3 resolveAnchorPosition() {
        Vec3 p = state.castAnchorPos();
        return p != null ? p : position();
    }
}
