package com.pgalaxyp.fragmento.content.bard.entity;

import java.util.UUID;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.movement.ConstantSpeedHomingMovement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntityBasic extends TimedSkillEntity<FluteSkillEntityBasic.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    private static final double SPEED_PER_TICK = 0.55;
    private static final double BOUNCE_IMPULSE = 0.28;
    private static final double BOUNCE_UP = 0.06;

    public FluteSkillEntityBasic(BardSkillEntityBase spirit) {
        super(spirit);
        startPhase(Phase.SPAWN, 10);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            s.setAnimKey(BardAnimKeys.SPAWN);
            s.flightController.setEnabled(false);
            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.TRAVEL) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setMovement(new ConstantSpeedHomingMovement<>(SPEED_PER_TICK));
            s.flightController.setEnabled(true);

            s.collisionController.setCollisionCheck(
                    com.pgalaxyp.fragmento.core.controller.CollisionController.adaptiveHomingHit(
                            s,
                            0.35,
                            1.10,
                            0.20
                    )
            );

            s.collisionController.resetCollision();
            s.collisionController.setEnabled(true);
            return;
        }

        if (phase == Phase.DESPAWN) {
            s.setAnimKey(BardAnimKeys.DESPAWN);
            s.flightController.setEnabled(false);
            s.collisionController.setEnabled(false);
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            if (time() >= duration()) {
                startPhase(Phase.TRAVEL, 12);
            }
            return;
        }

        if (phase == Phase.TRAVEL) {
            if (s.getTarget() == null) {
                startPhase(Phase.DESPAWN, 8);
                return;
            }

            if (s.collisionController.hasAnyCollision()) {
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

                applyBounce(s);
                startPhase(Phase.DESPAWN, 8);
                return;
            }

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, 8);
            }
            return;
        }

        if (phase == Phase.DESPAWN) {
            if (time() >= duration()) {
                s.discard();
            }
        }
    }

    private static void applyBounce(BardSkillEntityBase s) {
        Vec3 v = s.getDeltaMovement();
        Vec3 dir;

        if (v.lengthSqr() > 0.00000001) {
            dir = v.normalize();
        } else {
            LivingEntity t = s.getTarget();
            if (t != null) {
                Vec3 d = s.position().subtract(t.getBoundingBox().getCenter());
                dir = d.lengthSqr() > 0.00000001 ? d.normalize() : new Vec3(0.0, 0.0, 1.0);
            } else {
                dir = new Vec3(0.0, 0.0, 1.0);
            }
        }

        Vec3 back = dir.scale(-BOUNCE_IMPULSE).add(0.0, BOUNCE_UP, 0.0);
        s.impulseController.addImpulse(back);
    }
}
