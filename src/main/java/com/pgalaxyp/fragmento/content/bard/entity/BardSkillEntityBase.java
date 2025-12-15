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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

    private Vec3 smoothPos;
    private Vec3 smoothPosO;

    protected BardSkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);

        flightController = new AutoMovementController<>(this, BardSkillEntityBase::getTarget, BardSkillEntityBase::getLifetime);
        collisionController = new CollisionController<>(this, BardSkillEntityBase::getTarget);

        orientationController = new OrientationController<>(
                this,
                BardSkillEntityBase::getTarget,
                this::getLookAtPos
        );

        impulseController = new ImpulseController<>(this);
        spawnController = new SpawnController<>(this, (self, t) -> setTarget(t));

        controllers.add(spawnController);
        controllers.add(flightController);
        controllers.add(collisionController);
        controllers.add(impulseController);
        controllers.add(orientationController);
    }

    protected abstract SkillEntity createBehavior(SkillMode mode);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFETIME, 0);
        builder.define(CAST_STATE, BardSkillState.CastState.CHANNELING.ordinal());
        builder.define(ANIM_KEY, (byte) 0);
        builder.define(ORIENTATION_LOCKED, false);
    }

    @Override
    public void tick() {
        if (level().isClientSide()) {
            super.tick();
            clientSmoothTick();
            return;
        }
        super.tick();
    }

    private void clientSmoothTick() {
        Vec3 cur = position();

        if (smoothPos == null || smoothPosO == null) {
            smoothPos = cur;
            smoothPosO = cur;
            return;
        }

        smoothPosO = smoothPos;

        if (smoothPos.distanceToSqr(cur) > 64.0) {
            smoothPos = cur;
            smoothPosO = cur;
            return;
        }

        smoothPos = smoothPos.add(cur.subtract(smoothPos).scale(0.35));
    }

    public final Vec3 getSmoothRenderPos(float partialTick) {
        if (smoothPos == null || smoothPosO == null) {
            return new Vec3(
                    Mth.lerp(partialTick, xo, getX()),
                    Mth.lerp(partialTick, yo, getY()),
                    Mth.lerp(partialTick, zo, getZ())
            );
        }
        return com.pgalaxyp.fragmento.core.util.MathUtil.lerp(smoothPosO, smoothPos, partialTick);
    }

    @Override
    protected void preControllerTick() {
        if (state.castState() == BardSkillState.CastState.CHANNELING) {
            LivingEntity owner = getOwner();
            UUID source = state.sourceInstrumentUuid();
            if (!(owner instanceof ServerPlayer sp)) {
                requestDespawn();
            } else if (source == null || BardCatalystIdService.findInPlayerInventory(sp, source).isEmpty()) {
                requestDespawn();
            }
        }

        lifetimeTicks++;
        if (entityData.get(LIFETIME) != lifetimeTicks) {
            entityData.set(LIFETIME, lifetimeTicks);
        }

        int castOrdinal = state.castState().ordinal();
        if (entityData.get(CAST_STATE) != castOrdinal) {
            entityData.set(CAST_STATE, castOrdinal);
        }

        if (lifecycle.tick((ServerLevel) level())) {
            discard();
            return;
        }

        if (behavior != null) {
            behavior.tick();
            if (isRemoved()) return;
        }

        orientationController.setEnabled(!isOrientationLocked());
    }

    public final int getLifetime() {
        return lifetimeTicks;
    }

    public final void setAnimKey(byte key) {
        entityData.set(ANIM_KEY, key);
    }

    public final byte getAnimKey() {
        return entityData.get(ANIM_KEY);
    }

    public final boolean isOrientationLocked() {
        return entityData.get(ORIENTATION_LOCKED);
    }

    public final void setOrientationLocked(boolean locked) {
        entityData.set(ORIENTATION_LOCKED, locked);
    }

    public final void setLookAtPos(Vec3 pos) {
        this.lookAtPos = pos;
    }

    public final void clearLookAtPos() {
        this.lookAtPos = null;
    }

    public final Vec3 getLookAtPos() {
        return lookAtPos;
    }

    public final LivingEntity getOwner() {
        if (state.ownerUuid() == null) return null;
        if (!(level() instanceof ServerLevel sl)) return null;
        return sl.getPlayerByUUID(state.ownerUuid());
    }

    public final UUID getOwnerUuid() {
        return state.ownerUuid();
    }

    public final UUID getSourceInstrumentUuid() {
        return state.sourceInstrumentUuid();
    }

    public final LivingEntity getTarget() {
        int id = state.targetEntityId();
        if (id <= 0) return null;

        LivingEntity t = null;
        if (level() instanceof ServerLevel sl) {
            if (sl.getEntity(id) instanceof LivingEntity l) t = l;
        } else {
            if (level().getEntity(id) instanceof LivingEntity l) t = l;
        }

        return t != null && t.isAlive() ? t : null;
    }

    public final void setTarget(LivingEntity t) {
        state.setTargetEntityId(t != null ? t.getId() : 0);
    }

    public final boolean isCasted() {
        return lifecycle.isCasted();
    }

    public final void markCasted() {
        lifecycle.markCasted();
    }

    public final void requestDespawn() {
        lifecycle.requestDespawn(BardSpiritConstants.CASTED_SPIRIT_DESPAWN_DELAY_TICKS);
    }

    public final Vec3 resolveAnchorPosition() {
        Vec3 anchor = state.castAnchorPos();
        if (anchor != null) return anchor;

        LivingEntity t = getTarget();
        if (t != null) return t.position();

        return position();
    }

    protected final void onCastedInternal() {
        SkillEntity b = behavior;
        if (b != null) b.onCasted();
    }

    protected final void onCancelledInternal() {
        SkillEntity b = behavior;
        if (b != null) b.onCancelled();
    }

    public final int summon(
            ServerPlayer caster,
            LivingEntity target,
            ServerLevel level,
            SkillMode mode,
            net.minecraft.world.item.ItemStack sourceStack
    ) {
        if (caster == null || level == null || mode == null) return 0;

        state.setOwnerUuid(caster.getUUID());
        setTarget(target);

        state.setSourceInstrumentUuid(BardCatalystIdService.getOrCreate(sourceStack));

        behavior = createBehavior(mode);

        spawnController.initializeSpawn(
                caster,
                target,
                level,
                mode == SkillMode.CHARGED
        );

        return getId();
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
    public void push(Entity entity) {
    }
}
