package com.pgalaxyp.fragmento.rpg_old.content.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public final class SpeedZoneEntity extends Entity {

    private UUID ownerId;
    private int lifeTicks;

    public SpeedZoneEntity(EntityType<? extends SpeedZoneEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static UUID spawn(
            ServerLevel level,
            EntityType<SpeedZoneEntity> type,
            UUID ownerId,
            Vec3 pos,
            int lifeTicks
    ) {
        SpeedZoneEntity e = new SpeedZoneEntity(type, level);
        e.ownerId = ownerId;
        e.lifeTicks = Math.max(1, lifeTicks);
        e.setPos(pos.x, pos.y, pos.z);
        level.addFreshEntity(e);
        return e.getUUID();
    }

    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        if (tag.hasUUID("Owner")) this.ownerId = tag.getUUID("Owner");
        this.lifeTicks = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {
        if (ownerId != null) tag.putUUID("Owner", ownerId);
        tag.putInt("Life", lifeTicks);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (tickCount % 3 == 0) {
                double r = 3.5;
                double a = (tickCount * 0.2) % (Math.PI * 2.0);
                double px = getX() + Math.cos(a) * r;
                double pz = getZ() + Math.sin(a) * r;
                level().addParticle(ParticleTypes.END_ROD, px, getY() + 0.02, pz, 0.0, 0.01, 0.0);
            }
            return;
        }

        if (lifeTicks-- <= 0) {
            discard();
            return;
        }

        if (tickCount % 10 != 0) {
            return;
        }

        double r = 3.5;
        AABB box = new AABB(getX() - r, getY() - 1.0, getZ() - r, getX() + r, getY() + 2.0, getZ() + r);

        for (Player p : level().getEntitiesOfClass(Player.class, box, Player::isAlive)) {
            double dx = p.getX() - getX();
            double dz = p.getZ() - getZ();
            if (dx * dx + dz * dz <= r * r) {
                p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, true, true, true));
            }
        }
    }

    public float lifeProgress(float partial) {
        float t = (tickCount + partial) / Math.max(1.0f, (float) (tickCount + Math.max(0, lifeTicks)));
        return Mth.clamp(t, 0.0f, 1.0f);
    }
}