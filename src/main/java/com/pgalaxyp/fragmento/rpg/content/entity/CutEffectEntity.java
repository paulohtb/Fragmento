package com.pgalaxyp.fragmento.rpg.content.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class CutEffectEntity extends Entity {

    private static final EntityDataAccessor<Integer> DATA_LIFE =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_AGE =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_ORIENTATION =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Float> DATA_AIM_X =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AIM_Y =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_AIM_Z =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);

    private UUID ownerId;
    private UUID targetId;
    private float damage;

    public CutEffectEntity(EntityType<? extends CutEffectEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static UUID spawn(
            ServerLevel level,
            EntityType<CutEffectEntity> type,
            UUID ownerId,
            UUID targetId,
            Vec3 spawn,
            Vec3 target,
            int lifeTicks,
            float damage,
            CutOrientation orientation
    ) {
        if (level == null || type == null || spawn == null || target == null) return null;

        CutEffectEntity e = new CutEffectEntity(type, level);

        e.ownerId = ownerId;
        e.targetId = targetId;
        e.damage = Math.max(0f, damage);

        e.entityData.set(DATA_LIFE, Math.max(1, lifeTicks));
        e.entityData.set(DATA_AGE, 0);
        e.entityData.set(DATA_ORIENTATION, (byte) (orientation != null ? orientation.ordinal() : 0));

        e.entityData.set(DATA_AIM_X, (float) target.x);
        e.entityData.set(DATA_AIM_Y, (float) target.y);
        e.entityData.set(DATA_AIM_Z, (float) target.z);

        e.setPos(spawn);

        boolean added = level.addFreshEntity(e);
        return added ? e.getUUID() : null;
    }

    public static void spawnClient(
            Level level,
            EntityType<CutEffectEntity> type,
            Vec3 spawn,
            Vec3 target,
            int lifeTicks,
            CutOrientation orientation
    ) {
        if (level == null || type == null || spawn == null || target == null) return;

        CutEffectEntity e = new CutEffectEntity(type, level);

        e.ownerId = null;
        e.targetId = null;
        e.damage = 0f;

        e.entityData.set(DATA_LIFE, Math.max(1, lifeTicks));
        e.entityData.set(DATA_AGE, 0);
        e.entityData.set(DATA_ORIENTATION, (byte) (orientation != null ? orientation.ordinal() : 0));

        e.entityData.set(DATA_AIM_X, (float) target.x);
        e.entityData.set(DATA_AIM_Y, (float) target.y);
        e.entityData.set(DATA_AIM_Z, (float) target.z);

        e.setPos(spawn);
        level.addFreshEntity(e);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_LIFE, 10);
        builder.define(DATA_AGE, 0);
        builder.define(DATA_ORIENTATION, (byte) 0);
        builder.define(DATA_AIM_X, 0f);
        builder.define(DATA_AIM_Y, 0f);
        builder.define(DATA_AIM_Z, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Life", entityData.get(DATA_LIFE));
        tag.putInt("Age", entityData.get(DATA_AGE));
        tag.putByte("Ori", entityData.get(DATA_ORIENTATION));
        tag.putFloat("Ax", entityData.get(DATA_AIM_X));
        tag.putFloat("Ay", entityData.get(DATA_AIM_Y));
        tag.putFloat("Az", entityData.get(DATA_AIM_Z));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(DATA_LIFE, tag.getInt("Life"));
        entityData.set(DATA_AGE, tag.getInt("Age"));
        entityData.set(DATA_ORIENTATION, tag.getByte("Ori"));
        entityData.set(DATA_AIM_X, tag.getFloat("Ax"));
        entityData.set(DATA_AIM_Y, tag.getFloat("Ay"));
        entityData.set(DATA_AIM_Z, tag.getFloat("Az"));
    }

    public int life() {
        return entityData.get(DATA_LIFE);
    }

    public int age() {
        return entityData.get(DATA_AGE);
    }

    public CutOrientation orientation() {
        int o = entityData.get(DATA_ORIENTATION);
        if (o < 0 || o >= CutOrientation.values().length) {
            return CutOrientation.HORIZONTAL;
        }
        return CutOrientation.values()[o];
    }

    public Vec3 aimDir() {
        Vec3 aim = new Vec3(
                entityData.get(DATA_AIM_X),
                entityData.get(DATA_AIM_Y),
                entityData.get(DATA_AIM_Z)
        );
        return aim.subtract(position()).normalize();
    }

    @Override
    public void tick() {
        super.tick();

        int age = entityData.get(DATA_AGE) + 1;
        entityData.set(DATA_AGE, age);

        if (age >= entityData.get(DATA_LIFE)) {
            discard();
            return;
        }

        if (level().isClientSide) {
            return;
        }

        move(MoverType.SELF, moveDir().scale(0.6));

        if (!(level() instanceof ServerLevel sl)) {
            discard();
            return;
        }

        tryHit(sl);
    }

    private Vec3 moveDir() {
        Vec3 n = aimDir();
        CutOrientation o = orientation();
        if (o == CutOrientation.VERTICAL) {
            return new Vec3(n.x, Math.abs(n.y) < 0.2 ? 0.55 : n.y, n.z).normalize();
        }
        return new Vec3(n.x, 0.0, n.z).normalize();
    }

    private void tryHit(ServerLevel sl) {
        if (targetId == null || damage <= 0) return;

        Entity e = sl.getEntity(targetId);
        if (!(e instanceof LivingEntity le) || !le.isAlive()) return;

        Vec3 n = aimDir();
        Vec3 up = new Vec3(0, 1, 0);

        Vec3 u = up.cross(n).normalize();

        AABB hit = orientedBox(position(), u, up, n, 0.6, 0.6, 0.15);
        if (!hit.intersects(le.getBoundingBox())) return;

        DamageSource src = sl.damageSources().generic();

        Entity owner = ownerId != null ? sl.getEntity(ownerId) : null;
        if (owner instanceof LivingEntity ol) {
            src = sl.damageSources().mobAttack(ol);
        }

        le.hurt(src, damage);
        discard();
    }

    private static AABB orientedBox(
            Vec3 center,
            Vec3 u,
            Vec3 v,
            Vec3 n,
            double hu,
            double hv,
            double hn
    ) {
        Vec3 a = center.add(u.scale(hu)).add(v.scale(hv)).add(n.scale(hn));
        Vec3 b = center.add(u.scale(-hu)).add(v.scale(-hv)).add(n.scale(-hn));

        double minX = Math.min(a.x, b.x);
        double minY = Math.min(a.y, b.y);
        double minZ = Math.min(a.z, b.z);

        double maxX = Math.max(a.x, b.x);
        double maxY = Math.max(a.y, b.y);
        double maxZ = Math.max(a.z, b.z);

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}