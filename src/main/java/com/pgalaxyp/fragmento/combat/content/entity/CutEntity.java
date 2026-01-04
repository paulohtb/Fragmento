package com.pgalaxyp.fragmento.combat.content.entity;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public final class CutEntity extends Entity {

    private UUID ownerId;
    private UUID targetId;

    private Vec3 targetPoint;

    private int age;
    private int lifeTicks;
    private float damage;

    public CutEntity(EntityType<? extends CutEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static void spawn(
            ServerLevel level,
            EntityType<CutEntity> type,
            UUID ownerId,
            UUID targetId,
            Vec3 targetPoint,
            Vec3 spawn,
            int lifeTicks,
            float damage
    ) {
        CutEntity e = new CutEntity(type, level);
        e.ownerId = ownerId;
        e.targetId = targetId;
        e.targetPoint = targetPoint;
        e.lifeTicks = lifeTicks;
        e.damage = damage;
        e.setPos(spawn);
        level.addFreshEntity(e);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel sl)) {
            return;
        }

        age++;
        if (age > lifeTicks) {
            discard();
            return;
        }

        Vec3 aim = resolveAim(sl);
        Vec3 pos = position();

        double remaining = Math.max(1, lifeTicks - age);
        Vec3 to = aim.subtract(pos);
        double dist = to.length();

        if (dist < 0.01) {
            tryHit(sl);
            discard();
            return;
        }

        Vec3 move = to.scale(1.0 / remaining);
        setPos(pos.add(move));
    }

    private Vec3 resolveAim(ServerLevel sl) {
        if (targetId != null) {
            Entity e = sl.getEntity(targetId);
            if (e instanceof LivingEntity le && le.isAlive()) {
                targetPoint = le.getBoundingBox().getCenter();
            }
        }
        return targetPoint;
    }

    private void tryHit(ServerLevel sl) {
        if (targetId == null || damage <= 0) {
            return;
        }

        Entity e = sl.getEntity(targetId);
        if (!(e instanceof LivingEntity le) || !le.isAlive()) {
            return;
        }

        if (!le.getBoundingBox().contains(position())) {
            return;
        }

        DamageSource src = sl.damageSources().generic();
        if (ownerId != null) {
            Entity o = sl.getEntity(ownerId);
            if (o instanceof LivingEntity lo) {
                src = sl.damageSources().mobAttack(lo);
            }
        }

        le.hurt(src, damage);

        FragmentoLog.combat(
                "cut hit applied, target.uuid={} dmg={}",
                targetId,
                damage
        );
    }

    public int ageTicks() {
        return age;
    }

    public int lifeTicks() {
        return lifeTicks;
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("age");
        lifeTicks = tag.getInt("life");
        damage = tag.getFloat("dmg");

        if (tag.hasUUID("owner")) ownerId = tag.getUUID("owner");
        if (tag.hasUUID("target")) targetId = tag.getUUID("target");

        targetPoint = new Vec3(
                tag.getDouble("tx"),
                tag.getDouble("ty"),
                tag.getDouble("tz")
        );
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("age", age);
        tag.putInt("life", lifeTicks);
        tag.putFloat("dmg", damage);

        if (ownerId != null) tag.putUUID("owner", ownerId);
        if (targetId != null) tag.putUUID("target", targetId);

        if (targetPoint != null) {
            tag.putDouble("tx", targetPoint.x);
            tag.putDouble("ty", targetPoint.y);
            tag.putDouble("tz", targetPoint.z);
        }
    }
}