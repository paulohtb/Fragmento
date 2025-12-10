package com.pgalaxyp.fragmento.features.bard_class.spirit.type;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MelodyZone extends Entity {

    private int lifetime;
    private LivingEntity owner;
    private double radius = 3.0D;

    public MelodyZone(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.lifetime = tag.getInt("Lifetime");
        this.radius = tag.getDouble("Radius");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Lifetime", this.lifetime);
        tag.putDouble("Radius", this.radius);
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        if (this.level().isClientSide()) return;

        int maxLifetime = 100;
        if (lifetime >= maxLifetime) {
            discard();
            return;
        }

        Vec3 center = position();
        double r = radius;

        AABB area = new AABB(
                center.x - r,
                center.y - 1.0D,
                center.z - r,
                center.x + r,
                center.y + 1.0D,
                center.z + r
        );

        List<LivingEntity> list = level().getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e.isAlive()
        );

        for (LivingEntity e : list) {
            if (owner != null && e == owner) {
            }
            if (e instanceof Player) {
                e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0, false, false, true));
            }
            e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false, true));
        }
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
}
