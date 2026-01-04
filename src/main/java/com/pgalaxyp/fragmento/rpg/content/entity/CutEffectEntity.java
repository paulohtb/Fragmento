package com.pgalaxyp.fragmento.rpg.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public final class CutEffectEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_LIFE_TICKS =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> DATA_ORIENTATION =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> DATA_HIT =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.BOOLEAN);

    private UUID ownerId;
    private UUID targetId;
    private float damage;

    public CutEffectEntity(EntityType<? extends CutEffectEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static void spawn(
            ServerLevel level,
            EntityType<CutEffectEntity> type,
            UUID ownerId,
            UUID targetId,
            Vec3 targetPoint,
            Vec3 spawn,
            int lifeTicks,
            float damage,
            CutOrientation orientation
    ) {
        CutEffectEntity e = new CutEffectEntity(type, level);

        e.ownerId = ownerId;
        e.targetId = targetId;
        e.damage = damage;

        e.entityData.set(DATA_LIFE_TICKS, Math.max(1, lifeTicks));
        e.entityData.set(DATA_ORIENTATION, (byte) (orientation != null ? orientation.ordinal() : CutOrientation.VERTICAL.ordinal()));
        e.entityData.set(DATA_HIT, false);

        e.setPos(spawn);

        Vec3 dir = targetPoint.subtract(spawn);
        if (dir.lengthSqr() < 1.0E-8) dir = new Vec3(0.0, 0.0, 1.0);
        dir = dir.normalize();

        double speed = 0.8;
        e.setDeltaMovement(dir.scale(speed));
        e.applyRotFromDir(dir);

        level.addFreshEntity(e);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel sl)) return;

        if (tickCount > lifeTicks()) {
            discard();
            return;
        }

        if (entityData.get(DATA_HIT)) return;

        updateHoming(sl);

        Vec3 from = position();
        Vec3 to = from.add(getDeltaMovement());
        setPos(to);

        tryHit(sl, from, to);
    }

    private void updateHoming(ServerLevel sl) {
        if (targetId == null) return;

        Entity e = sl.getEntity(targetId);
        if (!(e instanceof LivingEntity le) || !le.isAlive()) return;

        Vec3 desired = le.getBoundingBox().getCenter().subtract(position());
        if (desired.lengthSqr() < 1.0E-8) return;
        desired = desired.normalize();

        Vec3 current = getDeltaMovement().normalize();
        Vec3 blended = current.scale(0.65).add(desired.scale(0.35)).normalize();

        double speed = 0.8;
        setDeltaMovement(blended.scale(speed));
        applyRotFromDir(blended);
    }

    private void tryHit(ServerLevel sl, Vec3 from, Vec3 to) {
        if (targetId == null || damage <= 0.0f) return;

        Entity e = sl.getEntity(targetId);
        if (!(e instanceof LivingEntity le) || !le.isAlive()) return;

        AABB box = le.getBoundingBox().inflate(0.6);
        Optional<Vec3> hit = box.clip(from, to);
        if (hit.isEmpty()) return;

        DamageSource src = sl.damageSources().generic();
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity lo) {
                src = sl.damageSources().mobAttack(lo);
            }
        }

        le.hurt(src, damage);
        entityData.set(DATA_HIT, true);
        setDeltaMovement(Vec3.ZERO);
    }

    private void applyRotFromDir(Vec3 dir) {
        float yaw = (float) (Mth.atan2(dir.x, dir.z) * 57.295776);
        float pitch = (float) (Mth.atan2(dir.y, Math.sqrt(dir.x * dir.x + dir.z * dir.z)) * 57.295776);

        setYRot(yaw);
        setXRot(pitch);
        yRotO = yaw;
        xRotO = pitch;
    }

    public int lifeTicks() {
        return entityData.get(DATA_LIFE_TICKS);
    }

    public CutOrientation getOrientation() {
        return CutOrientation.values()[entityData.get(DATA_ORIENTATION)];
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_LIFE_TICKS, 1);
        builder.define(DATA_ORIENTATION, (byte) 0);
        builder.define(DATA_HIT, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("owner")) ownerId = tag.getUUID("owner");
        if (tag.hasUUID("target")) targetId = tag.getUUID("target");

        damage = tag.getFloat("dmg");
        entityData.set(DATA_LIFE_TICKS, tag.getInt("life"));
        entityData.set(DATA_ORIENTATION, tag.getByte("orient"));
        entityData.set(DATA_HIT, tag.getBoolean("hit"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (ownerId != null) tag.putUUID("owner", ownerId);
        if (targetId != null) tag.putUUID("target", targetId);

        tag.putFloat("dmg", damage);
        tag.putInt("life", lifeTicks());
        tag.putByte("orient", entityData.get(DATA_ORIENTATION));
        tag.putBoolean("hit", entityData.get(DATA_HIT));
    }
}