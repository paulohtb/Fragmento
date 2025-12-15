package com.pgalaxyp.fragmento.gameplay.entity;

import com.pgalaxyp.fragmento.core.controller.EntityController;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.List;

public abstract class SkillEntityBase extends Entity {

    protected final List<EntityController<?>> controllers = new ArrayList<>(6);

    private Vec3 logicPos;
    private Vec3 prevLogicPos;

    protected SkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    protected void preControllerTick() {
    }

    public final Vec3 getLogicPos() {
        return logicPos != null ? logicPos : position();
    }

    public final Vec3 getPrevLogicPos() {
        return prevLogicPos != null ? prevLogicPos : getLogicPos();
    }

    public final void setLogicPos(Vec3 pos) {
        if (pos == null) return;
        logicPos = pos;
    }

    @Override
    public final void tick() {
        if (level().isClientSide()) {
            super.tick();
            return;
        }

        prevLogicPos = logicPos;
        if (logicPos == null) logicPos = position();

        preControllerTick();
        if (isRemoved()) return;

        for (EntityController<?> controller : controllers) {
            controller.tick();
            if (isRemoved()) return;
        }

        super.tick();
    }

    @Override
    public final void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        if (logicPos == null) {
            logicPos = new Vec3(x, y, z);
            prevLogicPos = logicPos;
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
