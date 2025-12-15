package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.movement.TimedGoalMovement;
import com.pgalaxyp.fragmento.core.controller.movement.TimedHomingMovement;
import com.pgalaxyp.fragmento.core.controller.movement.TimedLinearMovement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public final class FluteSkillEntityCharged
        extends TimedSkillEntity<FluteSkillEntityCharged.Phase> {

    enum Phase { SPAWN, TRAVEL, OVERSHOOT, ASCENT, HOVER, DESPAWN }

    private Vec3 overshootDir = Vec3.ZERO;
    private Vec3 ascentBase = Vec3.ZERO;
    private Vec3 hoverOffset = Vec3.ZERO;

    private boolean vortexSpawned;

    public FluteSkillEntityCharged(BardSkillEntityBase spirit) {
        super(spirit);
        vortexSpawned = false;
        startPhase(Phase.SPAWN, 8);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        switch (phase) {
            case SPAWN -> {
                s.setAnimKey(BardAnimKeys.SPAWN);
                s.flightController.setEnabled(false);
            }

            case TRAVEL -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);
                s.flightController.setMovement(
                        new TimedHomingMovement<>(
                                duration(),
                                (self, target) -> target.getBoundingBox().getCenter()
                        )
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        s.collisionController.segmentHit(0.25)
                );
                s.collisionController.setEnabled(true);
            }

            case OVERSHOOT -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);

                LivingEntity t = s.getTarget();
                if (t != null) {
                    Vec3 d = t.getBoundingBox().getCenter().subtract(s.position());
                    overshootDir = d.lengthSqr() < 1.0E-8 ? new Vec3(0, 0, 1) : d.normalize();
                } else {
                    overshootDir = new Vec3(0, 0, 1);
                }

                s.flightController.setMovement(
                        new TimedLinearMovement<>(duration(), overshootDir.scale(0.4))
                );
                s.flightController.setEnabled(true);
                s.collisionController.setEnabled(false);
            }

            case ASCENT -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);
                ascentBase = s.position();

                s.flightController.setMovement(
                        new TimedGoalMovement<>(
                                duration(),
                                () -> ascentBase.add(0.0, 2.0, 0.0),
                                0.25
                        )
                );
                s.flightController.setEnabled(true);
            }

            case HOVER -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);

                LivingEntity t = s.getTarget();
                if (t == null) {
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

                hoverOffset = s.position().subtract(t.position());
                vortexSpawned = false;

                s.flightController.setMovement(
                        (self, target, age) -> target.position().add(hoverOffset)
                );
                s.flightController.setEnabled(true);
            }

            case DESPAWN -> {
                s.setAnimKey(BardAnimKeys.DESPAWN);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
            }
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase != Phase.DESPAWN && s.getTarget() == null) {
            startPhase(Phase.DESPAWN, 8);
            return;
        }

        switch (phase) {
            case SPAWN -> {
                if (time() >= duration()) startPhase(Phase.TRAVEL, 8);
            }

            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    if (hit != null) {
                        hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
                    }
                    startPhase(Phase.OVERSHOOT, 4);
                    return;
                }
                if (time() >= duration()) startPhase(Phase.OVERSHOOT, 4);
            }

            case OVERSHOOT -> {
                if (time() >= duration()) startPhase(Phase.ASCENT, 6);
            }

            case ASCENT -> {
                if (time() >= duration()) startPhase(Phase.HOVER, 12);
            }

            case HOVER -> {
                if (!vortexSpawned && time() >= 5) {
                    spawnVortex();
                    vortexSpawned = true;
                }
                if (time() >= duration()) startPhase(Phase.DESPAWN, 8);
            }

            case DESPAWN -> {
                if (time() >= duration()) s.discard();
            }
        }
    }

    private void spawnVortex() {
        BardSkillEntityBase s = spirit();
        if (!(s.level() instanceof ServerLevel level)) return;

        LivingEntity owner = s.getOwner();
        if (!(owner instanceof ServerPlayer)) return;

        UUID ownerId = owner.getUUID();
        if (!WindVortexLimitService.tryReserve(level, ownerId, WindVortexLimitService.VortexTier.MINOR)) return;

        MinorWindVortex vortex = new MinorWindVortex(
                com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry.WIND_VORTEX.get(),
                level
        );

        vortex.setOwner(owner);
        vortex.markReservedCount();

        Vec3 p = s.getTarget() != null ? s.getTarget().position() : s.position();
        vortex.setPos(p.x, p.y, p.z);

        if (!level.addFreshEntity(vortex)) {
            WindVortexLimitService.release(level, ownerId, WindVortexLimitService.VortexTier.MINOR);
        }
    }

    @Override
    protected void onCancelled() {
        startPhase(Phase.DESPAWN, 8);
    }
}
