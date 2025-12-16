package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.CollisionController;
import com.pgalaxyp.fragmento.core.controller.movement.ConstantSpeedHomingMovement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;

public final class FluteSkillEntityBasic extends TimedSkillEntity<FluteSkillEntityBasic.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    private static final double SPEED_PER_TICK = 0.55;

    public FluteSkillEntityBasic(BardSkillEntityBase spirit) {
        super(spirit);
        startPhase(Phase.SPAWN, 10);
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
                        new ConstantSpeedHomingMovement<>(SPEED_PER_TICK)
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        CollisionController.segmentHit(0.35)
                );
                s.collisionController.resetCollision();
                s.collisionController.setEnabled(true);
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

        switch (phase) {
            case SPAWN -> {
                if (time() >= duration()) {
                    startPhase(Phase.TRAVEL, 12);
                }
            }

            case TRAVEL -> {
                if (s.getTarget() == null) {
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    if (hit != null) {
                        hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.BASIC_DAMAGE);

                        if (s.level() instanceof ServerLevel && s.getOwner() instanceof ServerPlayer sp) {
                            UUID src = s.getSourceInstrumentUuid();
                            ItemStack inst = BardCatalystIdService.findInPlayerInventory(sp, src);
                            if (!inst.isEmpty()) {
                                BardChargeData.increment(inst);
                            }
                        }
                    }
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

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
}
