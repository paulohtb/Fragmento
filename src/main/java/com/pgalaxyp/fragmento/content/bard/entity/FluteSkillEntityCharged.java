package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry;
import com.pgalaxyp.fragmento.core.controller.movement.ConstantSpeedHomingMovement;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
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
    private static final double ASCENT_SPEED = 0.25;
    private static final double FOLLOW_MAX_SPEED = 0.70;
    private static final double BOUNCE_IMPULSE = 0.30;
    private static final double BOUNCE_UP = 0.08;

    private Vec3 moveDir = new Vec3(0.0, 0.0, 1.0);
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

        if (phase == Phase.SPAWN) {
            s.setAnimKey(BardAnimKeys.SPAWN);
            s.flightController.setEnabled(false);
            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.TRAVEL) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setMovement(new ConstantSpeedHomingMovement<>(TRAVEL_SPEED));
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

        if (phase == Phase.OVERSHOOT) {
            s.flightController.setMovement((self, target) -> moveDir.scale(OVERSHOOT_SPEED));
            s.flightController.setEnabled(true);
            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.ASCENT) {
            s.flightController.setMovement((self, target) -> new Vec3(0.0, ASCENT_SPEED, 0.0));
            s.flightController.setEnabled(true);
            return;
        }

        if (phase == Phase.HOVER) {
            LivingEntity t = s.getTarget();
            if (t == null) {
                startPhase(Phase.DESPAWN, 8);
                return;
            }

            hoverOffset = s.position().subtract(t.position());
            vortexSpawned = false;

            s.flightController.setMovement((self, target) -> {
                if (target == null) return Vec3.ZERO;
                Vec3 desiredPos = target.position().add(hoverOffset);
                Vec3 delta = desiredPos.subtract(self.position());
                return MathUtil.clampLength(delta, FOLLOW_MAX_SPEED);
            });
            s.flightController.setEnabled(true);
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

        if (phase != Phase.DESPAWN && s.getTarget() == null) {
            startPhase(Phase.DESPAWN, 8);
            return;
        }

        if (phase == Phase.SPAWN) {
            if (time() >= duration()) startPhase(Phase.TRAVEL, 12);
            return;
        }

        if (phase == Phase.TRAVEL) {
            if (s.collisionController.hasAnyCollision()) {
                LivingEntity hit = s.collisionController.getCollisionTarget();
                if (hit != null) {
                    hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
                }

                captureMoveDir(s);
                applyBounce(s);
                startPhase(Phase.OVERSHOOT, 4);
                return;
            }

            if (time() >= duration()) {
                captureMoveDir(s);
                startPhase(Phase.OVERSHOOT, 4);
            }
            return;
        }

        if (phase == Phase.OVERSHOOT) {
            if (time() >= duration()) startPhase(Phase.ASCENT, 6);
            return;
        }

        if (phase == Phase.ASCENT) {
            if (time() >= duration()) startPhase(Phase.HOVER, 14);
            return;
        }

        if (phase == Phase.HOVER) {
            if (!vortexSpawned && time() >= 5) {
                spawnVortex();
                vortexSpawned = true;
            }
            if (time() >= duration()) startPhase(Phase.DESPAWN, 8);
            return;
        }

        if (phase == Phase.DESPAWN) {
            if (time() >= duration()) s.discard();
        }
    }

    private void captureMoveDir(BardSkillEntityBase s) {
        Vec3 d = s.getDeltaMovement();
        if (d.lengthSqr() > 0.00000001) {
            moveDir = d.normalize();
        }
    }

    private static void applyBounce(BardSkillEntityBase s) {
        Vec3 d = s.getDeltaMovement();
        Vec3 dir = d.lengthSqr() > 0.00000001 ? d.normalize() : new Vec3(0.0, 0.0, 1.0);

        Vec3 back = dir.scale(-BOUNCE_IMPULSE).add(0.0, BOUNCE_UP, 0.0);
        s.impulseController.addImpulse(back);
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

        EntityType<?> type = VortexHelperRegistry.WIND_VORTEX.get();

        MinorWindVortex vortex = new MinorWindVortex(type, level);
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
