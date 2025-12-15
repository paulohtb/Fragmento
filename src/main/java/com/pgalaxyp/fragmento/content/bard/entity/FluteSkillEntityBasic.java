package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.movement.TimedHomingMovement;
import net.minecraft.world.entity.LivingEntity;

public final class FluteSkillEntityBasic
        extends TimedSkillEntity<FluteSkillEntityBasic.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    public FluteSkillEntityBasic(BardSkillEntityBase spirit) {
        super(spirit);
        startPhase(Phase.SPAWN, 8);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();
        switch (phase) {
            case SPAWN -> s.setAnimKey(BardAnimKeys.SPAWN);
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
            case DESPAWN -> s.setAnimKey(BardAnimKeys.DESPAWN);
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.TRAVEL && s.collisionController.hasCollision()) {
            LivingEntity hit = s.collisionController.getCollisionTarget();
            if (hit != null) hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.BASIC_DAMAGE);
            startPhase(Phase.DESPAWN, 8);
        }

        if (time() >= duration() && phase != Phase.DESPAWN) {
            startPhase(Phase.DESPAWN, 8);
        }

        if (phase == Phase.DESPAWN && time() >= duration()) {
            s.discard();
        }
    }
}
