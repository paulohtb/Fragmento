package com.pgalaxyp.fragmento.gameplay.entity;

import com.pgalaxyp.fragmento.core.controller.EntityController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
        if (level().isClientSide()) {
            super.tick();
            return;
        }

        preControllerTick();
        if (isRemoved()) return;

        for (EntityController<?> controller : controllers) {
            controller.tick();
            if (isRemoved()) return;
        }

        Vec3 motion = getDeltaMovement();
        if (motion.lengthSqr() > 1.0E-10) {
            move(MoverType.SELF, motion);
            hasImpulse = true;
            hurtMarked = true;
        }

        super.tick();
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
