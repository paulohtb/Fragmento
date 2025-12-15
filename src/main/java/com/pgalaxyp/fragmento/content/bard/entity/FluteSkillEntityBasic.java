package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.movement.TimedHomingMovement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class FluteSkillEntityBasic
        extends TimedSkillEntity<FluteSkillEntityBasic.Phase> {

    enum Phase {
        SPAWN,
        TRAVEL,
        BOUNCE,
        DESPAWN
    }

    public FluteSkillEntityBasic(BardSkillEntityBase spirit) {
        super(spirit);

        spirit.setAnimKey(BardAnimKeys.SPAWN);

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);
        spirit.orientationController.setEnabled(true);

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
                s.orientationController.setEnabled(true);
                s.setDeltaMovement(Vec3.ZERO);
            }
            case TRAVEL -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);

                s.flightController.setMovement(
                        new TimedHomingMovement<>(
                                duration(),
                                0,
                                (self, target) -> target.getBoundingBox().getCenter()
                        )
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        s.collisionController.sweptStopBeforeHitbox(0.2)
                );
                s.collisionController.setScanIntervalTicks(1);
                s.collisionController.setScanOtherEntities(false);
                s.collisionController.setEnabled(true);

                s.orientationController.setEnabled(true);
            }
            case BOUNCE -> {
                s.setAnimKey(BardAnimKeys.TRAVEL);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.orientationController.setEnabled(true);
            }
            case DESPAWN -> {
                s.setAnimKey(BardAnimKeys.DESPAWN);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.orientationController.setEnabled(true);
                s.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase != Phase.DESPAWN) {
            LivingEntity t = s.getTarget();
            if (t == null) {
                startPhase(Phase.DESPAWN, 8);
                return;
            }
        }

        switch (phase) {
            case SPAWN -> {
                if (time() >= duration()) {
                    startPhase(Phase.TRAVEL, 10);
                }
            }
            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    s.collisionController.resetCollision();

                    if (hit != null && hit.isAlive()) {
                        onHit(hit);
                        applyImpulseFromHit(s, hit);
                    }

                    startPhase(Phase.BOUNCE, 3);
                    return;
                }

                if (time() >= duration()) {
                    startPhase(Phase.DESPAWN, 8);
                }
            }
            case BOUNCE -> {
                if (time() >= duration()) {
                    startPhase(Phase.DESPAWN, 8);
                }
            }
            case DESPAWN -> {
                if (time() >= duration()) {
                    s.discard();
                }
            }
        }
    }

    private void applyImpulseFromHit(
            BardSkillEntityBase s,
            LivingEntity hit
    ) {
        Vec3 dir = s.position().subtract(hit.position());
        if (dir.lengthSqr() < 1.0E-6) {
            dir = new Vec3(0.0, 0.2, 0.0);
        }

        Vec3 impulse = dir.normalize()
                .add(0.0, 0.2, 0.0)
                .scale(0.2);

        s.impulseController.applyImpulse(impulse);
    }

    private void onHit(LivingEntity target) {
        BardSkillEntityBase s = spirit();

        target.hurt(
                target.damageSources().magic(),
                BardInstrumentConstants.BASIC_DAMAGE
        );

        if (!(s.getOwner() instanceof ServerPlayer player)) return;

        UUID sourceId = s.getSourceInstrumentUuid();
        if (sourceId == null) return;

        var stack = BardCatalystIdService.findInPlayerInventory(player, sourceId);
        if (stack.isEmpty()) return;

        BardChargeData.increment(stack);
    }

    @Override
    protected void onCancelled() {
        startPhase(Phase.DESPAWN, 8);
    }
}
