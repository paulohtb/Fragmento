package com.pgalaxyp.fragmento.combat.engine.entity;

import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class CombatHitEntity extends Entity {

    protected UUID ownerId;
    protected float damage;
    protected ServerCombatState combatState;

    protected CombatHitEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            discard();
            return;
        }

        HitResult result = performHit(serverLevel);
        discard();
    }

    protected abstract HitResult performHit(
            net.minecraft.server.level.ServerLevel level
    );

    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
}