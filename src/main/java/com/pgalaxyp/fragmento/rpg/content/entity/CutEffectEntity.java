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

    private static final EntityDataAccessor<Float> DATA_MOVE_X =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_MOVE_Y =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_MOVE_Z =
            SynchedEntityData.defineId(CutEffectEntity.class, EntityDataSerializers.FLOAT);

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
    }

    public static void spawn(
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
        CutEffectEntity e = new CutEffectEntity(type, level);
        e.ownerId = ownerId;
        e.targetId = targetId;
        e.damage = damage;

        Vec3 moveDir = target.subtract(spawn).normalize();
        Vec3 aimDir = target.subtract(spawn).normalize();

        e.entityData.set(DATA_LIFE, Math.max(1, lifeTicks));
        e.entityData.set(DATA_AGE, 0);
        e.entityData.set(DATA_ORIENTATION, (byte) orientation.ordinal());

        e.entityData.set(DATA_MOVE_X, (float) moveDir.x);
        e.entityData.set(DATA_MOVE_Y, (float) moveDir.y);
        e.entityData.set(DATA_MOVE_Z, (float) moveDir.z);

        e.entityData.set(DATA_AIM_X, (float) aimDir.x);
        e.entityData.set(DATA_AIM_Y, (float) aimDir.y);
        e.entityData.set(DATA_AIM_Z, (float) aimDir.z);

        e.setPos(spawn);
        level.addFreshEntity(e);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            return;
        }

        int age = entityData.get(DATA_AGE) + 1;
        entityData.set(DATA_AGE, age);

        if (age >= entityData.get(DATA_LIFE)) {
            discard();
            return;
        }

        move(MoverType.SELF, moveDir().scale(0.6));

        if (level() instanceof ServerLevel sl) {
            tryHit(sl);
        }
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
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity lo) {
                src = sl.damageSources().mobAttack(lo);
            }
        }

        le.hurt(src, damage);
        damage = 0;
    }

    private static AABB orientedBox(Vec3 c, Vec3 u, Vec3 v, Vec3 n, double eu, double ev, double en) {
        Vec3 uu = u.scale(eu);
        Vec3 vv = v.scale(ev);
        Vec3 nn = n.scale(en);

        Vec3[] p = new Vec3[]{
                c.add(uu).add(vv).add(nn),
                c.add(uu).add(vv).subtract(nn),
                c.add(uu).subtract(vv).add(nn),
                c.add(uu).subtract(vv).subtract(nn),
                c.subtract(uu).add(vv).add(nn),
                c.subtract(uu).add(vv).subtract(nn),
                c.subtract(uu).subtract(vv).add(nn),
                c.subtract(uu).subtract(vv).subtract(nn)
        };

        double minX = p[0].x, minY = p[0].y, minZ = p[0].z;
        double maxX = minX, maxY = minY, maxZ = minZ;

        for (Vec3 vtx : p) {
            minX = Math.min(minX, vtx.x);
            minY = Math.min(minY, vtx.y);
            minZ = Math.min(minZ, vtx.z);
            maxX = Math.max(maxX, vtx.x);
            maxY = Math.max(maxY, vtx.y);
            maxZ = Math.max(maxZ, vtx.z);
        }

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public Vec3 moveDir() {
        return new Vec3(
                entityData.get(DATA_MOVE_X),
                entityData.get(DATA_MOVE_Y),
                entityData.get(DATA_MOVE_Z)
        );
    }

    public Vec3 aimDir() {
        return new Vec3(
                entityData.get(DATA_AIM_X),
                entityData.get(DATA_AIM_Y),
                entityData.get(DATA_AIM_Z)
        );
    }

    public CutOrientation getOrientation() {
        return CutOrientation.values()[entityData.get(DATA_ORIENTATION)];
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder b) {
        b.define(DATA_LIFE, 1);
        b.define(DATA_AGE, 0);
        b.define(DATA_ORIENTATION, (byte) 0);
        b.define(DATA_MOVE_X, 0f);
        b.define(DATA_MOVE_Y, 0f);
        b.define(DATA_MOVE_Z, 1f);
        b.define(DATA_AIM_X, 0f);
        b.define(DATA_AIM_Y, 0f);
        b.define(DATA_AIM_Z, 1f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}