package com.pgalaxyp.fragmento.NEW;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NewFluteSpecialEntity extends Entity {

    private static final EntityDataAccessor<Float> RADIUS =
            SynchedEntityData.defineId(NewFluteSpecialEntity.class, EntityDataSerializers.FLOAT);

    public NewFluteSpecialEntity(EntityType<? extends NewFluteSpecialEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RADIUS, 0f);
    }

    public void setRadius(float radius) {
        this.getEntityData().set(RADIUS, radius);
    }

    public float getRadius() {
        return this.getEntityData().get(RADIUS);
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = this.level().getNearestPlayer(this, 3);
        if (owner != null) {
            this.setPos(owner.getX(), owner.getY() + 0.25f, owner.getZ());
            this.setYRot(owner.getYRot());
            this.setXRot(owner.getXRot());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
