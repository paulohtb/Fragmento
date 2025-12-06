package com.pgalaxyp.fragmento.feature.bard_class.common.spirit;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.pgalaxyp.fragmento.feature.bard_class.common.data.SpiritAnimationData;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class SpiritTargetBase extends SpiritBase {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final TargetController<SpiritTargetBase> targetController;
    protected final OrientationController<SpiritTargetBase> orientationController;
    protected final FlightController<SpiritTargetBase> flightController;
    protected final HitController<SpiritTargetBase> hitController;
    protected final SpawnSoundController<SpiritTargetBase> spawnSoundController;

    protected SpiritTargetBase(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);

        LOGGER.info("[Spirit {}] Constructing SpiritTargetBase", this.getId());

        this.targetController = new TargetController<>(this);
        this.orientationController = new OrientationController<>(this, this.targetController);
        this.flightController = new FlightController<>(this, this.targetController);

        this.hitController = new HitController<>(this, this.targetController, this.flightController) {
            @Override
            protected void onTargetHit(LivingEntity target) {
                LOGGER.info("[Spirit {}] Hit target {}", SpiritTargetBase.this.getId(), target.getName().getString());
                SpiritTargetBase.this.entityData.set(SpiritAnimationData.HIT, true);
                onTargetHitInternal(target);
            }

            @Override
            protected void onTargetLost() {
                LOGGER.info("[Spirit {}] Target lost, starting despawn", SpiritTargetBase.this.getId());
                SpiritTargetBase.this.entityData.set(SpiritAnimationData.HIT, true);
            }

            @Override
            protected void onFlightFinished() {
                LOGGER.info("[Spirit {}] Flight finished, despawning", SpiritTargetBase.this.getId());
                SpiritTargetBase.this.onFlightFinished();
            }
        };

        this.spawnSoundController = new SpawnSoundController<>(this) {
            @Override
            protected void play() {
                LOGGER.info("[Spirit {}] Spawn sound triggered", SpiritTargetBase.this.getId());
                onSpawnSound();
            }
        };

        this.addController(this.targetController);
        this.addController(this.orientationController);
        this.addController(this.flightController);
        this.addController(this.spawnSoundController);
        this.addController(this.hitController);
    }

    public void setTarget(LivingEntity target) {
        LOGGER.info("[Spirit {}] setTarget={}", this.getId(), target.getName().getString());
        this.targetController.setTarget(target);
    }

    public LivingEntity getTarget() {
        return this.targetController.getTarget();
    }

    public void configureFlight(int idleTicks, int travelTicks, double collisionRadius) {
        LOGGER.info("[Spirit {}] configureFlight idle={} travel={} radius={}",
                this.getId(), idleTicks, travelTicks, collisionRadius);
        this.flightController.configure(idleTicks, travelTicks, collisionRadius);
    }

    public void setDespawnDurationTicks(int ticks) {
        LOGGER.info("[Spirit {}] setDespawnDurationTicks={}", this.getId(), ticks);
        this.hitController.setDespawnDuration(ticks);
    }

    public boolean hasHit() {
        return this.entityData.get(SpiritAnimationData.HIT);
    }

    public Vec3 getTargetPosition() {
        LivingEntity t = this.getTarget();
        return t == null ? this.position() : t.getBoundingBox().getCenter();
    }

    public void faceInstantlyTowards(Vec3 targetPos) {
        LOGGER.info("[Spirit {}] faceInstantlyTowards {}", this.getId(), targetPos);
        this.orientationController.faceInstantly(targetPos);
    }

    protected abstract void onTargetHitInternal(LivingEntity target);

    protected void onFlightFinished() {}

    protected void onSpawnSound() {}
}
