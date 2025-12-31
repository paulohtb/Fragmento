package com.pgalaxyp.fragmento.combat.content.entity.flute;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public final class FluteCastingEntity extends Entity {

    private long targetId;
    private java.util.UUID ownerId;

    public FluteCastingEntity(EntityType<? extends FluteCastingEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void configure(
            net.minecraft.server.level.ServerPlayer player,
            long targetId
    ) {
        this.ownerId = player.getUUID();
        this.targetId = targetId;
    }

    @Override
    public void tick() {
        super.tick();
        discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}