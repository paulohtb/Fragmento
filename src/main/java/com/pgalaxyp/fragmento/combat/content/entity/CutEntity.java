package com.pgalaxyp.fragmento.combat.content.entity;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public final class CutEntity extends Entity {

    private static final double HIT_RADIUS = 0.35;

    private UUID ownerId;
    private UUID targetId;

    private int age;
    private int lifeTicks;

    private double aimX;
    private double aimY;
    private double aimZ;

    private boolean guided;
    private float damage;

    public CutEntity(EntityType<? extends CutEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static void spawn(
            ServerLevel level,
            EntityType<CutEntity> type,
            LivingEntity owner,
            LivingEntity target,
            Vec3 spawnPos,
            Vec3 aimPoint,
            int lifeTicks,
            float damage,
            boolean verticalOnly
    ) {
        if (level == null || type == null || owner == null || spawnPos == null || aimPoint == null) {
            FragmentoLog.combat(
                    "cut spawn ignore, levelNull={} typeNull={} ownerNull={} spawnNull={} aimNull={}",
                    level == null,
                    type == null,
                    owner == null,
                    spawnPos == null,
                    aimPoint == null
            );
            return;
        }

        CutEntity e = new CutEntity(type, level);
        e.ownerId = owner.getUUID();
        e.targetId = target != null ? target.getUUID() : null;

        e.age = 0;
        e.lifeTicks = Math.max(1, lifeTicks);
        e.damage = Math.max(0.0f, damage);

        e.aimX = aimPoint.x;
        e.aimY = aimPoint.y;
        e.aimZ = aimPoint.z;

        e.guided = !verticalOnly;

        e.setPos(spawnPos);
        e.faceTowards(new Vec3(e.aimX, e.aimY, e.aimZ), verticalOnly);

        level.addFreshEntity(e);

        FragmentoLog.combat(
                "cut spawned, eid={} owner.uuid={} target.uuid={} guided={} verticalOnly={} lifeTicks={} dmg={} spawn={} aim={}",
                e.getId(),
                e.ownerId,
                e.targetId,
                e.guided,
                verticalOnly,
                e.lifeTicks,
                e.damage,
                vec(spawnPos),
                vec(aimPoint)
        );
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("age");
        lifeTicks = tag.getInt("life");
        damage = tag.getFloat("dmg");
        guided = tag.getBoolean("g");

        if (tag.contains("owner")) ownerId = tag.getUUID("owner");
        if (tag.contains("target")) targetId = tag.getUUID("target");

        aimX = tag.getDouble("ax");
        aimY = tag.getDouble("ay");
        aimZ = tag.getDouble("az");

        FragmentoLog.combat(
                "cut read nbt, eid={} age={} life={} dmg={} guided={} owner.uuid={} target.uuid={} aim={}",
                getId(),
                age,
                lifeTicks,
                damage,
                guided,
                ownerId,
                targetId,
                vec(new Vec3(aimX, aimY, aimZ))
        );
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("age", age);
        tag.putInt("life", lifeTicks);
        tag.putFloat("dmg", damage);
        tag.putBoolean("g", guided);

        if (ownerId != null) tag.putUUID("owner", ownerId);
        if (targetId != null) tag.putUUID("target", targetId);

        tag.putDouble("ax", aimX);
        tag.putDouble("ay", aimY);
        tag.putDouble("az", aimZ);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            return;
        }

        age++;
        if (age > lifeTicks) {
            FragmentoLog.combat(
                    "cut discard expired, eid={} age={} lifeTicks={} owner.uuid={} target.uuid={}",
                    getId(),
                    age,
                    lifeTicks,
                    ownerId,
                    targetId
            );
            discard();
            return;
        }

        Vec3 aim = resolveAim();
        Vec3 pos = position();

        Vec3 to = aim.subtract(pos);
        double dist = to.length();

        if (dist <= HIT_RADIUS) {
            FragmentoLog.combat(
                    "cut hit radius reached, eid={} age={} dist={} owner.uuid={} target.uuid={} pos={} aim={}",
                    getId(),
                    age,
                    dist,
                    ownerId,
                    targetId,
                    vec(pos),
                    vec(aim)
            );
            tryApplyHit();
            discard();
            return;
        }

        int remaining = Math.max(1, lifeTicks - age + 1);
        Vec3 dir = to.scale(1.0 / Math.max(0.0001, dist));
        Vec3 move = dir.scale(dist / remaining);

        setPos(pos.add(move));
        faceTowards(aim, false);

        if ((age % 10) == 0) {
            FragmentoLog.combat(
                    "cut tick, eid={} age={} lifeTicks={} remaining={} guided={} dist={} pos={} aim={}",
                    getId(),
                    age,
                    lifeTicks,
                    remaining,
                    guided,
                    dist,
                    vec(position()),
                    vec(aim)
            );
        }
    }

    private Vec3 resolveAim() {
        if (!(level() instanceof ServerLevel sl)) {
            return new Vec3(aimX, aimY, aimZ);
        }

        if (guided && targetId != null) {
            Entity e = sl.getEntity(targetId);
            if (e instanceof LivingEntity le && le.isAlive()) {
                Vec3 c = le.getBoundingBox().getCenter();
                double dx = Math.abs(c.x - aimX);
                double dy = Math.abs(c.y - aimY);
                double dz = Math.abs(c.z - aimZ);

                aimX = c.x;
                aimY = c.y;
                aimZ = c.z;

                if ((age % 10) == 0 || dx > 0.5 || dy > 0.5 || dz > 0.5) {
                    FragmentoLog.combat(
                            "cut resolveAim guided update, eid={} age={} target.uuid={} target.eid={} aimNow={} delta=(%.3f,%.3f,%.3f)",
                            getId(),
                            age,
                            targetId,
                            le.getId(),
                            vec(c),
                            dx,
                            dy,
                            dz
                    );
                }

                return c;
            } else {
                if ((age % 10) == 0) {
                    FragmentoLog.combat(
                            "cut resolveAim guided lost target, eid={} age={} target.uuid={} found={} alive={}",
                            getId(),
                            age,
                            targetId,
                            e != null,
                            e instanceof LivingEntity le2 && le2.isAlive()
                    );
                }
            }
        }

        return new Vec3(aimX, aimY, aimZ);
    }

    private void faceTowards(Vec3 aim, boolean verticalOnly) {
        Vec3 d = aim.subtract(position());

        if (verticalOnly) {
            setXRot(-90.0f);
            xRotO = -90.0f;
            return;
        }

        float yaw = (float) (Mth.atan2(d.z, d.x) * (180.0 / Math.PI)) - 90.0f;
        setYRot(yaw);
        yRotO = yaw;
    }

    private void tryApplyHit() {
        if (!(level() instanceof ServerLevel sl)) {
            FragmentoLog.combat("cut tryApplyHit ignore, notServerLevel=true eid={}", getId());
            return;
        }
        if (targetId == null || damage <= 0.0f) {
            FragmentoLog.combat(
                    "cut tryApplyHit ignore, targetNullOrNoDamage eid={} target.uuid={} dmg={}",
                    getId(),
                    targetId,
                    damage
            );
            return;
        }

        Entity t = sl.getEntity(targetId);
        if (!(t instanceof LivingEntity target) || !target.isAlive()) {
            FragmentoLog.combat(
                    "cut tryApplyHit ignore, targetMissingOrDead eid={} target.uuid={} found={} alive={}",
                    getId(),
                    targetId,
                    t != null,
                    t instanceof LivingEntity le && le.isAlive()
            );
            return;
        }

        AABB box = getBoundingBox().inflate(HIT_RADIUS);
        List<LivingEntity> hits = sl.getEntitiesOfClass(LivingEntity.class, box, e ->
                e.isAlive() && e.getUUID().equals(targetId)
        );

        if (hits.isEmpty()) {
            FragmentoLog.combat(
                    "cut tryApplyHit miss, eid={} target.uuid={} box={}",
                    getId(),
                    targetId,
                    box.toString()
            );
            return;
        }

        DamageSource src = sl.damageSources().generic();
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity le) {
                src = sl.damageSources().mobAttack(le);
            }
        }

        boolean ok = target.hurt(src, damage);

        FragmentoLog.combat(
                "cut tryApplyHit applied, eid={} owner.uuid={} target.uuid={} target.eid={} dmg={} ok={} target.hpAfter={}",
                getId(),
                ownerId,
                targetId,
                target.getId(),
                damage,
                ok,
                target.getHealth()
        );
    }

    public int ageTicks() {
        return age;
    }

    public int lifeTicks() {
        return lifeTicks;
    }

    private static String vec(Vec3 v) {
        if (v == null) {
            return "null";
        }
        return String.format("(%.3f,%.3f,%.3f)", v.x, v.y, v.z);
    }
}