package com.pgalaxyp.fragmento.features.bard_class.spirit.base;

import com.pgalaxyp.fragmento.core.controller.*;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.SpiritBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CastedSpiritBase extends Entity {

    public static final EntityDataAccessor<Integer> LIFETIME =
            SynchedEntityData.defineId(CastedSpiritBase.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<String> ANIM_KEY =
            SynchedEntityData.defineId(CastedSpiritBase.class, EntityDataSerializers.STRING);

    public enum Mode {
        BASIC,
        CHARGED,
        SPECIAL
    }

    private final List<EntityController<?>> controllers = new ArrayList<>(5);
    private LivingEntity owner;
    private LivingEntity target;
    private UUID ownerUuid;
    private UUID targetUuid;
    private ItemStack instrumentSnapshot = ItemStack.EMPTY;

    public final FlightController<CastedSpiritBase> flightController;
    public final CollisionController<CastedSpiritBase> collisionController;
    public final BounceController<CastedSpiritBase> spiritBounceController;
    public final OrientationController<CastedSpiritBase> orientationController;
    public final SpawnController<CastedSpiritBase> spawnController;

    private SpiritBehavior behavior;

    public CastedSpiritBase(EntityType<? extends CastedSpiritBase> type, Level level) {
        super(type, level);
        this.noPhysics = true;

        this.flightController = new FlightController<>(this, CastedSpiritBase::getTarget, CastedSpiritBase::getLifetime);
        this.collisionController = new CollisionController<>(this, CastedSpiritBase::getTarget);
        this.spiritBounceController = new BounceController<>(this, CastedSpiritBase::getTarget);
        this.orientationController = new OrientationController<>(this, CastedSpiritBase::getTarget);
        this.spawnController = new SpawnController<>(this, CastedSpiritBase::setTarget);

        controllers.add(spawnController);
        controllers.add(flightController);
        controllers.add(orientationController);
        controllers.add(spiritBounceController);
        controllers.add(collisionController);
    }

    public abstract SpiritBehavior createBehavior(Mode mode);

    public final SpiritBehavior behaviorInternal() {
        return behavior;
    }

    public void summon(LivingEntity caster,
                       LivingEntity target,
                       ServerLevel level,
                       Mode mode,
                       ItemStack instrumentStack) {

        this.owner = caster;
        this.target = target;

        this.ownerUuid = caster != null ? caster.getUUID() : null;
        this.targetUuid = target != null ? target.getUUID() : null;

        this.instrumentSnapshot = instrumentStack == null ? ItemStack.EMPTY : instrumentStack.copy();
        this.entityData.set(LIFETIME, 0);
        this.entityData.set(ANIM_KEY, "");
        this.behavior = createBehavior(mode);

        boolean charged = mode == Mode.CHARGED;
        this.spawnController.initializeSpawn(caster, target, level, charged);
    }

    public void summon(LivingEntity caster,
                       LivingEntity target,
                       ServerLevel level,
                       Mode mode) {
        summon(caster, target, level, mode, ItemStack.EMPTY);
    }

    public ItemStack getInstrumentSnapshot() {
        return instrumentSnapshot;
    }

    public LivingEntity getOwner() {
        if (owner == null && ownerUuid != null && level() instanceof ServerLevel sl) {
            ServerPlayer p = (ServerPlayer) sl.getPlayerByUUID(ownerUuid);
            if (p != null && p.isAlive()) owner = p;
        }
        return owner;
    }

    public LivingEntity getTarget() {
        if (target != null && target.isAlive()) return target;

        if (targetUuid == null) {
            target = null;
            return null;
        }

        if (level() instanceof ServerLevel sl) {
            ServerPlayer p = (ServerPlayer) sl.getPlayerByUUID(targetUuid);
            if (p != null && p.isAlive()) {
                target = p;
                return target;
            }

            AABB area = this.getBoundingBox().inflate(128.0);
            List<LivingEntity> list = sl.getEntitiesOfClass(
                    LivingEntity.class,
                    area,
                    e -> e.isAlive() && targetUuid.equals(e.getUUID())
            );
            if (!list.isEmpty()) {
                target = list.get(0);
                return target;
            }
        }

        target = null;
        return null;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
        this.targetUuid = target != null ? target.getUUID() : null;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public int getLifetime() {
        return this.entityData.get(LIFETIME);
    }

    public String getAnimKey() {
        return this.entityData.get(ANIM_KEY);
    }

    public void setAnimKey(String key) {
        this.entityData.set(ANIM_KEY, key == null ? "" : key);
    }

    public void onHit(LivingEntity target) {
        if (behavior != null) behavior.onHit(target);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            movement();
            return;
        }

        int age = getLifetime() + 1;
        this.entityData.set(LIFETIME, age);

        flightController.tick();
        orientationController.tick();
        collisionController.tick();
        spiritBounceController.tick();

        if (behavior != null) {
            behavior.tick();
        }

        movement();
    }

    private void movement() {
        Vec3 v = this.getDeltaMovement();
        if (v.lengthSqr() > 0) this.move(MoverType.SELF, v);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LIFETIME, 0);
        builder.define(ANIM_KEY, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(LIFETIME, tag.getInt("Lifetime"));
        this.entityData.set(ANIM_KEY, tag.getString("AnimKey"));

        if (tag.hasUUID("OwnerUUID")) this.ownerUuid = tag.getUUID("OwnerUUID");
        if (tag.hasUUID("TargetUUID")) this.targetUuid = tag.getUUID("TargetUUID");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.entityData.get(LIFETIME));
        tag.putString("AnimKey", this.entityData.get(ANIM_KEY));

        if (ownerUuid != null) tag.putUUID("OwnerUUID", ownerUuid);
        if (targetUuid != null) tag.putUUID("TargetUUID", targetUuid);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
