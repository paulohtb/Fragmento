package com.pgalaxyp.fragmento.NEW;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NewSoundWaveEntity extends Entity {

    private int life = 0;

    public NewSoundWaveEntity(EntityType<? extends NewSoundWaveEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        life = life + 1;
        if (life >= 5) discard();
    }

    public float getWaveScale(float partialTicks) {
        int stage = life;
        float scale;
        switch(stage) {
            case 0:
                scale = 0.325f;
                break;
            case 1:
                scale = 0.550f;
                break;
            case 2:
                scale = 0.775f;
                break;
            case 3:
                scale = 0.900f;
                break;
            default:
                scale = 1.00f;
        }
        return scale;
    }

    public int getStage() {
        return life;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}
}
