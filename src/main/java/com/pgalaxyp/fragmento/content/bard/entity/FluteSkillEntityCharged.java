package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry;
import com.pgalaxyp.fragmento.core.controller.CollisionController;
import com.pgalaxyp.fragmento.core.controller.movement.ConstantSpeedHomingMovement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntityCharged extends TimedSkillEntity<FluteSkillEntityCharged.Phase> {

    enum Phase {
        SPAWN,
        TRAVEL,
        OVERSHOOT,
        ASCENT,
        HOVER,
        DESPAWN
    }

    private static final double TRAVEL_SPEED = 0.55;
    private static final double OVERSHOOT_SPEED = 0.35;

    private Vec3 moveDir = Vec3.ZERO;
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
                s.collisionController.setEnabled(false);
            }

            case TRAVEL -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);

                s.flightController.setMovement(
                        new ConstantSpeedHomingMovement<>(TRAVEL_SPEED)
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        CollisionController.segmentHit(0.35)
                );
                s.collisionController.resetCollision();
                s.collisionController.setEnabled(true);
            }

            case OVERSHOOT -> {
                s.flightController.setMovement(
                        (self, target) -> self.position().add(moveDir.scale(OVERSHOOT_SPEED))
                );
                s.flightController.setEnabled(true);
                s.collisionController.setEnabled(false);
            }

            case ASCENT -> {
                s.flightController.setMovement(
                        (self, target) -> self.position().add(0.0, 0.25, 0.0)
                );
                s.flightController.setEnabled(true);
            }

            case HOVER -> {
                LivingEntity t = s.getTarget();
                if (t == null) {
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

                hoverOffset = s.position().subtract(t.position());
                vortexSpawned = false;

                s.flightController.setMovement(
                        (self, target) -> target.position().add(hoverOffset)
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
                if (time() >= duration()) startPhase(Phase.TRAVEL, 12);
            }

            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    if (hit != null) {
                        hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
                    }

                    Vec3 d = s.getDeltaMovement();
                    if (d.lengthSqr() > 1.0E-8) moveDir = d.normalize();

                    startPhase(Phase.OVERSHOOT, 4);
                    return;
                }

                if (time() >= duration()) {
                    Vec3 d = s.getDeltaMovement();
                    if (d.lengthSqr() > 1.0E-8) moveDir = d.normalize();
                    startPhase(Phase.OVERSHOOT, 4);
                }
            }

            case OVERSHOOT -> {
                if (time() >= duration()) startPhase(Phase.ASCENT, 6);
            }

            case ASCENT -> {
                if (time() >= duration()) startPhase(Phase.HOVER, 14);
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

        if (!WindVortexLimitService.tryReserve(
                level,
                owner.getUUID(),
                WindVortexLimitService.VortexTier.MINOR
        )) return;

        MinorWindVortex vortex = new MinorWindVortex(
                VortexHelperRegistry.WIND_VORTEX.get(),
                level
        );

        vortex.setOwner(owner);
        vortex.markReservedCount();

        Vec3 p = s.getTarget() != null ? s.getTarget().position() : s.position();
        vortex.setPos(p.x, p.y, p.z);

        if (!level.addFreshEntity(vortex)) {
            WindVortexLimitService.release(
                    level,
                    owner.getUUID(),
                    WindVortexLimitService.VortexTier.MINOR
            );
        }
    }
}
