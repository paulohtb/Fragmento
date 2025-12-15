package com.pgalaxyp.fragmento.gameplay.entity;

import com.pgalaxyp.fragmento.core.controller.EntityController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillEntityBase extends Entity {

    protected final List<EntityController<?>> controllers = new ArrayList<>(6);

    protected SkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    protected void preControllerTick() {
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            preControllerTick();

            for (EntityController<?> controller : controllers) {
                controller.tick();
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
