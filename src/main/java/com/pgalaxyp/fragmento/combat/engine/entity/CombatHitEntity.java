package com.pgalaxyp.fragmento.combat.engine.entity;

import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;
import com.pgalaxyp.fragmento.combat.rule.combo.ComboApplier;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public abstract class CombatHitEntity extends Entity {

    protected UUID ownerId;
    protected float damage;
    protected boolean resolved;

    protected ServerCombatState combatState;
    protected ComboApplier comboApplier;
    protected ComboDefinition comboDefinition;

    protected CombatHitEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.resolved = false;
    }

    public void configure(
            UUID ownerId,
            float damage,
            ServerCombatState state,
            ComboApplier applier,
            ComboDefinition combo
    ) {
        this.ownerId = ownerId;
        this.damage = Math.max(0.0f, damage);
        this.combatState = state;
        this.comboApplier = applier;
        this.comboDefinition = combo;
    }

    @Override
    public void tick() {
        super.tick();

        if (resolved) {
            discard();
            return;
        }

        if (!(level() instanceof ServerLevel serverLevel)) {
            discard();
            return;
        }

        resolve(serverLevel);
    }

    protected void resolve(ServerLevel level) {
        HitResult result = performHit(level);

        if (result != null) {
            comboApplier.applyHit(combatState, comboDefinition, result);
        }

        resolved = true;
    }

    protected abstract HitResult performHit(ServerLevel level);

    protected LivingEntity resolveOwner(ServerLevel level) {
        if (ownerId == null) {
            return null;
        }
        Entity e = level.getPlayerByUUID(ownerId);
        if (e instanceof LivingEntity living) {
            return living;
        }
        return null;
    }

    protected HitResult damageTarget(
            ServerLevel level,
            LivingEntity owner,
            LivingEntity target
    ) {
        if (owner == null || target == null) {
            return HitResult.miss();
        }
        if (!target.isAlive()) {
            return HitResult.miss();
        }

        DamageSource source = level.damageSources().mobAttack(owner);
        boolean damaged = target.hurt(source, damage);

        if (!damaged) {
            return HitResult.hitNoDamage();
        }

        return HitResult.damage();
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
}