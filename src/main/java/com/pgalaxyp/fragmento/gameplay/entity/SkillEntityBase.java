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

    protected final List<EntityController<?>> controllers =
            new ArrayList<>(6);

    private Vec3 prevPos;

    protected SkillEntityBase(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (level().isClientSide()) {
            clientTick();
            super.tick();
            return;
        }

        prevPos = position();

        serverPreControllers();

        for (EntityController<?> controller : controllers) {
            controller.tick();
            if (isRemoved()) return;
        }

        serverPostControllers();

        super.tick();
    }

    protected void clientTick() {
    }

    protected void serverPreControllers() {
    }

    protected void serverPostControllers() {
    }

    public final Vec3 getPrevPos() {
        return prevPos != null ? prevPos : position();
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
