package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl;

import com.pgalaxyp.fragmento.feature.bard_class.common.config.FluteConstants;
import com.pgalaxyp.fragmento.feature.bard_class.common.init.FluteVortexRegistry;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.BardSpirit;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FluteSpirit extends BardSpirit {

    private enum ChargedPhase {
        NONE,
        TRAVELING,
        MOVING_PAST_TARGET,
        MOVING_TO_HOVER,
        HOVERING,
        DESPAWNING
    }

    private ChargedPhase chargedPhase = ChargedPhase.NONE;
    private Vec3 chargedTargetCenter;
    private double chargedTargetFootY;
    private Vec3 chargedOvershootPos;
    private Vec3 chargedHoverPos;
    private int chargedMoveTicksTotal;
    private int chargedMoveTicksElapsed;
    private int hoverTicksRemaining;
    private int despawnTicksRemaining;
    private boolean vortexSpawned;

    public FluteSpirit(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    @Override
    public void setCharged(boolean v) {
        super.setCharged(v);
        if (v) {
            this.hitController.setEnabled(false);
            this.chargedPhase = ChargedPhase.TRAVELING;
        } else {
            this.hitController.setEnabled(true);
            this.chargedPhase = ChargedPhase.NONE;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            return;
        }
        if (this.isCharged()) {
            this.tickChargedServer();
        }
    }

    private void tickChargedServer() {
        if (this.chargedPhase == ChargedPhase.NONE) {
            this.chargedPhase = ChargedPhase.TRAVELING;
        }

        if (this.chargedPhase == ChargedPhase.DESPAWNING) {
            updateChargedDespawn();
            return;
        }

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) {
            if (this.chargedPhase != ChargedPhase.DESPAWNING) {
                startChargedDespawn();
            }
            updateChargedDespawn();
            return;
        }

        if (this.chargedPhase == ChargedPhase.TRAVELING) {
            updateChargedTravel(target);
            return;
        }

        if (this.chargedPhase == ChargedPhase.MOVING_PAST_TARGET || this.chargedPhase == ChargedPhase.MOVING_TO_HOVER) {
            updateChargedManualMovement();
            return;
        }

        if (this.chargedPhase == ChargedPhase.HOVERING) {
            updateChargedHover(target);
        }
    }

    private void updateChargedTravel(LivingEntity target) {
        Vec3 center = target.getBoundingBox().getCenter();
        Vec3 start = new Vec3(this.xOld, this.yOld, this.zOld);
        Vec3 end = this.position();
        Vec3 d = end.subtract(start);
        double lenSq = d.lengthSqr();
        if (lenSq < 1.0e-6) {
            return;
        }

        Vec3 toCenter = center.subtract(start);
        double t = toCenter.dot(d) / lenSq;
        if (t < 0.0 || t > 1.0) {
            return;
        }

        Vec3 closest = start.add(d.scale(t));
        double radius = FluteConstants.CHARGED_IMPACT_RADIUS;
        double distSq = closest.distanceToSqr(center);
        if (distSq > radius * radius) {
            return;
        }

        Vec3 dir = d.normalize();
        startChargedSequence(target, center, dir);
    }

    private void startChargedSequence(LivingEntity target, Vec3 center, Vec3 travelDir) {
        this.chargedTargetCenter = center;
        this.chargedTargetFootY = target.getY();
        this.chargedOvershootPos = center.add(travelDir.scale(FluteConstants.CHARGED_OVERSHOOT_DISTANCE));
        this.chargedHoverPos = new Vec3(
                this.chargedOvershootPos.x,
                center.y + FluteConstants.CHARGED_HOVER_HEIGHT,
                this.chargedOvershootPos.z
        );
        this.flightController.setEnabled(false);
        this.chargedPhase = ChargedPhase.MOVING_PAST_TARGET;
        this.chargedMoveTicksTotal = FluteConstants.CHARGED_OVERSHOOT_TICKS;
        this.chargedMoveTicksElapsed = 0;
    }

    private void updateChargedManualMovement() {
        Vec3 targetPos = this.chargedPhase == ChargedPhase.MOVING_PAST_TARGET ? this.chargedOvershootPos : this.chargedHoverPos;
        if (targetPos == null) {
            startChargedDespawn();
            return;
        }

        Vec3 current = this.position();
        Vec3 diff = targetPos.subtract(current);
        double distSq = diff.lengthSqr();
        if (distSq < 1.0e-4) {
            onChargedStepArrived();
            return;
        }

        int remaining = Math.max(1, this.chargedMoveTicksTotal - this.chargedMoveTicksElapsed);
        Vec3 vel = diff.scale(1.0D / remaining);
        this.setVelocity(vel);
        this.chargedMoveTicksElapsed++;

        if (this.chargedMoveTicksElapsed >= this.chargedMoveTicksTotal) {
            onChargedStepArrived();
        }
    }

    private void onChargedStepArrived() {
        if (this.chargedPhase == ChargedPhase.MOVING_PAST_TARGET) {
            this.chargedPhase = ChargedPhase.MOVING_TO_HOVER;
            this.chargedMoveTicksElapsed = 0;
            this.chargedMoveTicksTotal = FluteConstants.CHARGED_HOVER_MOVE_TICKS;
            return;
        }
        if (this.chargedPhase == ChargedPhase.MOVING_TO_HOVER) {
            this.setVelocity(Vec3.ZERO);
            this.chargedPhase = ChargedPhase.HOVERING;
            this.hoverTicksRemaining = FluteConstants.CHARGED_HOVER_DURATION_TICKS;
        }
    }

    private void updateChargedHover(LivingEntity currentTarget) {
        if (this.hoverTicksRemaining > 0) {
            this.hoverTicksRemaining--;
            if (this.hoverTicksRemaining > 0) {
                return;
            }
        }
        if (!this.vortexSpawned) {
            spawnVortex(currentTarget);
            this.vortexSpawned = true;
        }
        startChargedDespawn();
    }

    private void spawnVortex(LivingEntity currentTarget) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Vec3 center = this.chargedTargetCenter;
        double footY = this.chargedTargetFootY;

        if (center == null) {
            if (currentTarget != null) {
                center = currentTarget.getBoundingBox().getCenter();
                footY = currentTarget.getY();
            } else {
                Vec3 pos = this.position();
                center = pos;
                footY = pos.y;
            }
        }

        FluteVortex vortex = new FluteVortex(FluteVortexRegistry.FLUTE_VORTEX.get(), serverLevel);
        vortex.setOwner(this.getOwner());
        vortex.setPos(center.x, footY, center.z);
        serverLevel.addFreshEntity(vortex);

        serverLevel.playSound(
                null,
                center.x,
                footY,
                center.z,
                SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.PLAYERS,
                0.7F,
                1.0F
        );
    }

    private void startChargedDespawn() {
        if (this.chargedPhase == ChargedPhase.DESPAWNING) {
            return;
        }
        this.chargedPhase = ChargedPhase.DESPAWNING;
        this.despawnTicksRemaining = FluteConstants.CHARGED_DESPAWN_TICKS;
        this.setVelocity(Vec3.ZERO);
        this.startDespawnVisual();
    }

    private void updateChargedDespawn() {
        if (this.despawnTicksRemaining > 0) {
            this.despawnTicksRemaining--;
            if (this.despawnTicksRemaining <= 0) {
                this.discard();
            }
        }
    }

    @Override
    protected float getBasicDamage() {
        return FluteConstants.BASIC_DAMAGE;
    }

    @Override
    protected float getChargedDamage() {
        return FluteConstants.CHARGED_DAMAGE;
    }

    @Override
    protected void applyChargedHitEffects(LivingEntity target) {
        target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                FluteConstants.CHARGED_GLOWING_DURATION_TICKS,
                0,
                false,
                true,
                true
        ));
    }

    @Override
    protected void playHitSound(LivingEntity target) {
        target.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                0.8F,
                1.2F
        );
    }
}
